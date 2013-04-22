/**
 * Generated by Gas3 v2.3.0 (Granite Data Services).
 *
 * NOTE: this file is only generated if it does not exist. You may safely put
 * your custom code here.
 */

package org.openforis.collect.metamodel.ui.proxy {
	import org.openforis.collect.util.CollectionUtil;

    [Bindable]
    [RemoteClass(alias="org.openforis.collect.metamodel.ui.proxy.FormProxy")]
    public class FormProxy extends FormProxyBase {
		
		/**
		 * Traverse each child and pass its parent and itself  to the argument function
		 * */
		override public function traverse(funct:Function):void {
			for each (var formSection:FormSectionProxy in formSections) {
				funct(this, formSection);
				formSection.traverse(funct);
			}
			for each (var form:FormProxy in forms) {
				funct(this, form);
				form.traverse(funct);
			}
		}
		
		[Bindable(event="formSectionsChange")]
		public function get formSection():FormSectionProxy {
			if ( CollectionUtil.isEmpty(formSections) ) {
				return null;
			} else {
				return FormSectionProxy(formSections.getItemAt(0));
			}
		}
		
    }
}