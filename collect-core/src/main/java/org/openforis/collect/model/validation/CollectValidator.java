/**
 * 
 */
package org.openforis.collect.model.validation;

import static org.openforis.collect.model.FieldSymbol.CONFIRMED;

import java.util.List;

import org.openforis.collect.manager.RecordManager;
import org.openforis.collect.model.CollectRecord;
import org.openforis.collect.model.CollectRecord.Step;
import org.openforis.idm.metamodel.KeyAttributeDefinition;
import org.openforis.idm.metamodel.validation.ValidationResult;
import org.openforis.idm.metamodel.validation.ValidationResultFlag;
import org.openforis.idm.metamodel.validation.ValidationResults;
import org.openforis.idm.metamodel.validation.Validator;
import org.openforis.idm.model.Attribute;
import org.openforis.idm.model.Field;
import org.openforis.idm.model.Record;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author M. Togna
 * 
 */
public class CollectValidator extends Validator {
	
	@Autowired
	private RecordManager recordManager;

	@Override
	public ValidationResults validate(Attribute<?, ?> attribute) {
		ValidationResults results = new ValidationResults();
		
		SpecifiedValidator specifiedValidator = new SpecifiedValidator();
		ValidationResultFlag specifiedResultFlag = specifiedValidator.evaluate(attribute);
		results.addResult(specifiedValidator, specifiedResultFlag);

		if ( !specifiedResultFlag.isError() ) {
			if ( isRootEntityKey(attribute) ) {
				validateRootEntityKey(attribute, results);
			}
			
			CollectRecord record = (CollectRecord) attribute.getRecord();
			Step step = record.getStep();
			
			ValidationResults idmResults = super.validate(attribute);
			
			boolean confirmed = ( step == Step.ENTRY ) ? isValueConfirmed(attribute) : false;
			List<ValidationResult> errors = idmResults.getErrors();
			for (ValidationResult error : errors) {
				ValidationResultFlag newFlag = confirmed ? ValidationResultFlag.WARNING : ValidationResultFlag.ERROR;
				results.addResult(error.getValidator(), newFlag);
			}
			results.addResults(idmResults.getWarnings());
		}
		return results;

	}

	private void validateRootEntityKey(Attribute<?, ?> attribute, ValidationResults results) {
		RecordKeyUniquenessValidator keyValidator = new RecordKeyUniquenessValidator(recordManager);
		ValidationResultFlag res = keyValidator.evaluate(attribute);
		if(res == ValidationResultFlag.ERROR){
			results.addResult(keyValidator, ValidationResultFlag.ERROR);
		}
	}

	private boolean isRootEntityKey(Attribute<?, ?> attribute) {
		Record record = attribute.getRecord();
		return attribute.getDefinition() instanceof KeyAttributeDefinition && record.getRootEntity().equals(attribute.getParent());
	}

	static boolean isValueConfirmed(Attribute<?, ?> attribute) {
		int fieldCount = attribute.getFieldCount();
		for (int i = 0; i < fieldCount; i++) {
			Field<?> field = attribute.getField(i);
			Character symbol = field.getSymbol();

			if (!CONFIRMED.getSymbol().equals(symbol)) {
				return false;
			}
		}
		return true;
	}
	
}
