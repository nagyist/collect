/**
 * Generated by Gas3 v2.2.0 (Granite Data Services).
 *
 * NOTE: this file is only generated if it does not exist. You may safely put
 * your custom code here.
 */

package org.openforis.collect.metamodel.proxy {
	import mx.collections.IList;
	
	import org.openforis.collect.Application;

    [Bindable]
    [RemoteClass(alias="org.openforis.collect.metamodel.proxy.LanguageSpecificTextProxy")]
    public class LanguageSpecificTextProxy extends LanguageSpecificTextProxyBase {
		
		public static function getLocalizedText(list:IList, language:String, defaultLanguageCode:String):String {
			var text:String = getTextByLanguage(list, language);
			if ( text == null ) {
				text = getTextByLanguage(list, defaultLanguageCode);
				if ( text == null ) {
					text = getTextByLanguage(list, null);
				}
			}
			return text;
		}
		
		public static function getTextByLanguage(list:IList, language:String):String {
			for each (var item:LanguageSpecificTextProxy in list) {
				if ( item.language == language) {
					return item.text;
				}
			}
			return null;
		}
    }
}