package org.openforis.collect.presenter {
	import flash.events.Event;
	import flash.events.FocusEvent;
	import flash.events.KeyboardEvent;
	import flash.ui.Keyboard;
	
	import mx.binding.utils.ChangeWatcher;
	import mx.collections.IList;
	import mx.rpc.AsyncResponder;
	import mx.rpc.IResponder;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	
	import org.openforis.collect.Application;
	import org.openforis.collect.client.ClientFactory;
	import org.openforis.collect.client.DataClient;
	import org.openforis.collect.event.ApplicationEvent;
	import org.openforis.collect.event.InputFieldEvent;
	import org.openforis.collect.metamodel.proxy.AttributeDefinitionProxy;
	import org.openforis.collect.model.FieldSymbol;
	import org.openforis.collect.model.proxy.AttributeProxy;
	import org.openforis.collect.model.proxy.FieldProxy;
	import org.openforis.collect.model.proxy.NodeStateProxy;
	import org.openforis.collect.remoting.service.UpdateRequest;
	import org.openforis.collect.remoting.service.UpdateRequest$Method;
	import org.openforis.collect.remoting.service.UpdateResponse;
	import org.openforis.collect.ui.ContextMenuBuilder;
	import org.openforis.collect.ui.component.input.InputField;
	import org.openforis.collect.util.ArrayUtil;
	import org.openforis.collect.util.CollectionUtil;
	import org.openforis.collect.util.StringUtil;
	import org.openforis.collect.util.UIUtil;
	
	/**
	 * 
	 * @author M. Togna
	 * @author S. Ricci
	 * */
	public class InputFieldPresenter extends AbstractPresenter {
		
		public static const SHORTCUT_BLANK_ON_FORM:String = "*";
		public static const SHORTCUT_DASH_ON_FORM:String = "-";
		public static const SHORTCUT_ILLEGIBLE:String = "?";
		
		private var _view:InputField;
		private var _changed:Boolean = false;
		protected var _updateResponder:IResponder;
		private var _dataClient:DataClient;
		
		public function InputFieldPresenter(inputField:InputField = null) {
			_view = inputField;
			_dataClient = ClientFactory.dataClient;
			
			_updateResponder = new AsyncResponder(updateResultHandler, updateFaultHandler);
			
			super();
			updateView();
		}
		
		override internal function initEventListeners():void {
			super.initEventListeners();
			
			eventDispatcher.addEventListener(ApplicationEvent.UPDATE_RESPONSE_RECEIVED, updateResponseReceivedHandler);
			
			if(_view.textInput != null) {
				_view.textInput.addEventListener(KeyboardEvent.KEY_DOWN, keyDownHandler);
				_view.textInput.addEventListener(Event.CHANGE, changeHandler);
				_view.textInput.addEventListener(FocusEvent.FOCUS_OUT, focusOutHandler);
				_view.textInput.addEventListener(FocusEvent.FOCUS_IN, focusInHandler);
			}
			
			ChangeWatcher.watch(_view, "attribute", attributeChangeHandler);
		}
		
		protected function updateResponseReceivedHandler(event:ApplicationEvent):void {
			if(_view.attribute != null) {
				var response:UpdateResponse = UpdateResponse(event.result);
				if(response != null) {
					for each (var nodeState:NodeStateProxy in response.states) {
						if(nodeState.nodeId == _view.attribute.id) {
							updateView();
							return;
						}
					}
				}
			}
		}
		
		protected function attributeChangeHandler(event:Event):void {
			updateView();
		}
		
		protected function changeHandler(event:Event):void {
			//TODO if autocomplete enabled show autocomplete popup...
			_changed = true;
			var inputFieldEvent:InputFieldEvent = new InputFieldEvent(InputFieldEvent.CHANGING);
			_view.dispatchEvent(inputFieldEvent);
		}
		
		protected function focusOutHandler(event:FocusEvent):void {
			if(_view.applyChangesOnFocusOut && _changed) {
				applyValue();
			} else {
				//TODO perform validation only
			}
		}
		
		protected function keyDownHandler(event:KeyboardEvent):void {
			var keyCode:uint = event.keyCode;
			switch(keyCode) {
				case Keyboard.ESCAPE:
					undoLastChange();
					break;
			}
		}
		
		public function applyValue():void {
			var symbol:FieldSymbol = null;
			var value:String = null;
			var text:String = textToRequestValue();
			if(isShortCutForReasonBlank(text)) {
				symbol = parseShortCutForReasonBlank(text);
			} else {
				value = text;
			}
			var remarks:String = remarks; //preserve old remarks
			sendUpdateRequest(value, symbol, remarks);
		}
		
		public function applySymbol(symbol:FieldSymbol):void {
			var value:String = textToRequestValue();
			var remarks:String = remarks;
			sendUpdateRequest(value, symbol, remarks);
		}
		
		public function applySymbolAndRemarks(symbol:FieldSymbol, remarks:String):void {
			sendUpdateRequest(null, symbol, remarks);
		}
		
		protected function sendUpdateRequest(value:String, symbol:FieldSymbol = null, remarks:String = null):void {
			var req:UpdateRequest = new UpdateRequest();
			var def:AttributeDefinitionProxy = _view.attributeDefinition;
			req.parentEntityId = _view.parentEntity.id;
			req.nodeName = def.name;
			req.fieldIndex = _view.fieldIndex;
			req.symbol = symbol;
			req.remarks = remarks;
			req.value = value;
			if(_view.attribute != null) {
				var a:AttributeProxy = _view.attribute;
				req.nodeId = a.id;
				req.method = UpdateRequest$Method.UPDATE;
			} else {
				req.method = UpdateRequest$Method.ADD;
			}
			dataClient.updateActiveRecord(_updateResponder, req);
		}
		
		public function undoLastChange():void {
			_changed = false;
			updateView();
		}
		
		protected function focusInHandler(event:FocusEvent):void {
			UIUtil.ensureElementIsVisible(event.target);
		}
		
		protected function updateResultHandler(event:ResultEvent, token:Object = null):void {
			var response:UpdateResponse = UpdateResponse(event.result);
			Application.activeRecord.update(response);
			var appEvt:ApplicationEvent = new ApplicationEvent(ApplicationEvent.UPDATE_RESPONSE_RECEIVED);
			appEvt.result = response;
			eventDispatcher.dispatchEvent(appEvt);
			_changed = false;
			//_view.currentState = InputField.STATE_SAVE_COMPLETE;
		}

		protected function updateFaultHandler(event:FaultEvent, token:Object = null):void {
			//_view.currentState = InputField.STATE_ERROR_SAVING;
			undoLastChange();
			faultHandler(event, token);
		}
		
		protected function valueToText():String {
			var attribute:AttributeProxy = _view.attribute;
			if(attribute != null) {
				var field:FieldProxy = _view.attribute.getField(_view.fieldIndex);
				if(field.symbol != null) {
					var shortKey:String = getShortCutForReasonBlank(field.symbol);
					if(shortKey != null) {
						return shortKey;
					}
				}
				var value:Object = field.value;
				if(value != null && StringUtil.isNotBlank(value.toString())) {
					return value.toString();
				}
			}
			return "";
		}

		protected function textToRequestValue():String {
			var result:String = null;
			var text:String = _view.text;
			if(StringUtil.isNotBlank(text)) {
				result = StringUtil.trim(text);
			}
			return result;
		}
		
		protected function updateView():void {
			//update view according to attribute (generic text value)
			var remarksPresent:Boolean = false;
			if(_view.attributeDefinition != null) {
				var text:String = valueToText();
				_view.text = text;
				if(_view.attribute != null) {
					remarksPresent = StringUtil.isNotBlank(remarks);
				}
				_view.contextMenu = ContextMenuBuilder.buildContextMenu(_view);
			}
			_view.remarksPresent = remarksPresent;
		}
		
		protected function get field():FieldProxy {
			if(_view.attribute != null) {
				var fieldIndex:int = 0;
				if(_view.fieldIndex >= 0) {
					fieldIndex = _view.fieldIndex;
				}
				return _view.attribute.getField(fieldIndex);
			}
			return null;
		}
		
		protected function get remarks():String {
			var f:FieldProxy = field;
			if(f != null) {
				return f.remarks;
			} 
			return null;
		}
		
		public static function getShortCutForReasonBlank(symbol:FieldSymbol):String {
			switch(symbol) {
				case FieldSymbol.BLANK_ON_FORM:
					return SHORTCUT_BLANK_ON_FORM;
				case FieldSymbol.DASH_ON_FORM:
					return SHORTCUT_DASH_ON_FORM;
				case FieldSymbol.ILLEGIBLE:
					return SHORTCUT_ILLEGIBLE;
				default:
					return null;
			}
		}
		
		public static function parseShortCutForReasonBlank(text:String):FieldSymbol {
			switch(text) {
				case SHORTCUT_BLANK_ON_FORM:
					return FieldSymbol.BLANK_ON_FORM;
				case SHORTCUT_DASH_ON_FORM:
					return FieldSymbol.DASH_ON_FORM;
				case SHORTCUT_ILLEGIBLE:
					return FieldSymbol.ILLEGIBLE;
				default:
					return null;
			}
		}
		
		public static function isShortCutForReasonBlank(text:String):Boolean {
			return ArrayUtil.isIn([SHORTCUT_BLANK_ON_FORM, SHORTCUT_DASH_ON_FORM, SHORTCUT_ILLEGIBLE], text);
		}
		
		protected function get dataClient():DataClient {
			return _dataClient;
		}

		[Bindable]
		protected function get changed():Boolean {
			return _changed;
		}
		
		protected function set changed(value:Boolean):void {
			_changed = value;
		}
		
		[Bindable]
		protected function get updateResponder():IResponder {
			return _updateResponder;
		}
		
		protected function set updateResponder(value:IResponder):void {
			_updateResponder = value;
		}
		
		
		
	}
}
