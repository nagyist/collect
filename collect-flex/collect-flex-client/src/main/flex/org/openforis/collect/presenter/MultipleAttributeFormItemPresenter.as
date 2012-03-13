package org.openforis.collect.presenter
{
	import flash.events.FocusEvent;
	import flash.events.MouseEvent;
	
	import mx.collections.IList;
	import mx.core.UIComponent;
	import mx.rpc.events.ResultEvent;
	
	import org.openforis.collect.client.ClientFactory;
	import org.openforis.collect.event.ApplicationEvent;
	import org.openforis.collect.event.InputFieldEvent;
	import org.openforis.collect.model.proxy.AttributeProxy;
	import org.openforis.collect.remoting.service.UpdateRequest;
	import org.openforis.collect.remoting.service.UpdateRequestOperation;
	import org.openforis.collect.remoting.service.UpdateRequestOperation$Method;
	import org.openforis.collect.remoting.service.UpdateResponse;
	import org.openforis.collect.ui.component.detail.MultipleAttributeFormItem;
	import org.openforis.collect.ui.component.detail.ValidationDisplayManager;
	import org.openforis.collect.ui.component.input.InputField;
	import org.openforis.collect.util.UIUtil;

	/**
	 * 
	 * @author S. Ricci
	 *  
	 */
	public class MultipleAttributeFormItemPresenter extends FormItemPresenter {
		
		public function MultipleAttributeFormItemPresenter(view:MultipleAttributeFormItem) {
			super(view);
		}
		
		override internal function initEventListeners():void {
			super.initEventListeners();
			
			view.addButton.addEventListener(MouseEvent.CLICK, addButtonClickHandler);
			view.addButton.addEventListener(FocusEvent.FOCUS_IN, addButtonFocusInHandler);
			eventDispatcher.addEventListener(InputFieldEvent.VISITED, inputFieldVisitedHandler);
		}
		
		private function get view():MultipleAttributeFormItem {
			return MultipleAttributeFormItem(_view);
		}
		
		override protected function updateResponseReceivedHandler(event:ApplicationEvent):void {
			super.updateResponseReceivedHandler(event);
			if(_view.parentEntity != null) {
				var responses:IList = IList(event.result);
				for each (var response:UpdateResponse in responses) {
					if(response.nodeId == _view.parentEntity.id) {
						updateValidationDisplayManager();
						updateRelevanceDisplayManager();
						break;
					}
				}
			}
		}
		
		protected function inputFieldVisitedHandler(event:InputFieldEvent):void {
			var inputField:InputField = event.inputField;
			if(inputField != null && inputField.parentEntity != null && inputField.attributeDefinition == view.attributeDefinition) {
				updateValidationDisplayManager();
			}
		}
		
		override protected function updateView():void {
			if(view.dataGroup != null && view.parentEntity != null) {
				var attributes:IList = getAttributes();
				view.dataGroup.dataProvider = attributes;
			}
		}

		protected function getAttributes():IList {
			var name:String = view.attributeDefinition.name;
			var attributes:IList = view.parentEntity.getChildren(name);
			return attributes;
		}
		
		protected function addButtonFocusInHandler(event:FocusEvent):void {
			UIUtil.ensureElementIsVisible(event.target);
		}
		
		protected function addButtonClickHandler(event:MouseEvent):void {
			var attributes:IList = getAttributes();
			//var maxCount:Number = view.attributeDefinition.maxCount
			//if(isNaN(maxCount) || CollectionUtil.isEmpty(attributes) || attributes.length < maxCount) {
				var o:UpdateRequestOperation = new UpdateRequestOperation();
				o.method = UpdateRequestOperation$Method.ADD;
				o.parentEntityId = view.parentEntity.id;
				o.nodeName = view.attributeDefinition.name;
				var req:UpdateRequest = new UpdateRequest(o);
				ClientFactory.dataClient.updateActiveRecord(req, null, addResultHandler);
			/*} else {
				var labelText:String = view.attributeDefinition.getLabelText();
				AlertUtil.showError("edit.maxCountExceed", [maxCount, labelText]);
			}*/	
		}
		
		protected function addResultHandler(event:ResultEvent, token:Object = null):void {
			view.callLater(function():void {
				UIUtil.ensureElementIsVisible(view.addButton);
			});
		}
		
		override protected function initValidationDisplayManager():void {
			var validationStateDisplay:UIComponent = _view;
			var validationToolTipTrigger:UIComponent = validationStateDisplay;
			_validationDisplayManager = new ValidationDisplayManager(validationToolTipTrigger, validationStateDisplay);
			if(view.attributeDefinition != null) {
				updateValidationDisplayManager();
			}
		}
		
		override protected function updateRelevanceDisplayManager():void {
			_relevanceDisplayManager.displayNodeRelevance(view.parentEntity, view.attributeDefinition);
		}
		
		override protected function updateValidationDisplayManager():void {
			super.updateValidationDisplayManager();
			var visited:Boolean = isVisited();
			var detached:Boolean = isDetached();
			var active:Boolean = visited || ! detached;
			if(active) {
				_validationDisplayManager.active = true;
				_validationDisplayManager.displayNodeValidation(view.parentEntity, view.attributeDefinition);
			} else {
				_validationDisplayManager.active = false;
				_validationDisplayManager.reset();
			}
		}
		
		protected function isVisited():Boolean {
			var attributes:IList = getAttributes();
			for each (var a:AttributeProxy in attributes) {
				if(a.visited) {
					return true;
				}
			}
			return false;
		}
		
		protected function isDetached():Boolean {
			var attributes:IList = getAttributes();
			for each (var a:AttributeProxy in attributes) {
				if(! a.detached) {
					return false;
				}
			}
			return true;
		}

	}
}