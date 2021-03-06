package org.openforis.collect.presenter
{
	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	import mx.events.MenuEvent;
	
	import org.openforis.collect.event.UIEvent;
	import org.openforis.collect.i18n.Message;
	import org.openforis.collect.ui.component.UserPopUpButton;
	import org.openforis.collect.ui.view.Footer;

	/**
	 * 
	 * @author S. Ricci
	 * 
	 */
	public class UserPopUpButtonPresenter extends AbstractPresenter {
		
		private const CHANGE_PASSWORD_MENU_ITEM:String = Message.get("usersManagement.changePassword");
		private const LOGOUT_MENU_ITEM:String = Message.get("global.logout");
		
		public function UserPopUpButtonPresenter(view:UserPopUpButton) {
			super(view);
		}
		
		override public function init():void {
			super.init();
			initUserPopUpMenu();
		}
		
		private function get view():UserPopUpButton {
			return UserPopUpButton(_view);
		}
		
		override protected function initEventListeners():void {
			super.initEventListeners();
			view.loggedPopUpButton.addEventListener(MenuEvent.ITEM_CLICK, loggedPopUpMenuItemClickHandler);
		}
		
		protected function initUserPopUpMenu():void {
			var result:IList = new ArrayCollection();
			result.addItem(CHANGE_PASSWORD_MENU_ITEM);
			result.addItem({type: 'separator'});
			result.addItem(LOGOUT_MENU_ITEM);
			view.loggedPopUpButton.dataProvider = result;
		}
		
		protected function loggedPopUpMenuItemClickHandler(event:MenuEvent):void {
			switch ( event.item ) {
			case CHANGE_PASSWORD_MENU_ITEM:
				eventDispatcher.dispatchEvent(new UIEvent(UIEvent.CHANGE_PASSWORD_CLICK));
				break;
			case LOGOUT_MENU_ITEM:
				eventDispatcher.dispatchEvent(new UIEvent(UIEvent.LOGOUT_CLICK));
				break;
			}
		}
	}
}