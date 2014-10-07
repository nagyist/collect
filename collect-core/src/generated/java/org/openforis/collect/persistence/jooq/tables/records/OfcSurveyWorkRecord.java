/**
 * This class is generated by jOOQ
 */
package org.openforis.collect.persistence.jooq.tables.records;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = { "http://www.jooq.org", "3.4.2" },
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class OfcSurveyWorkRecord extends org.jooq.impl.UpdatableRecordImpl<org.openforis.collect.persistence.jooq.tables.records.OfcSurveyWorkRecord> implements org.jooq.Record4<java.lang.Integer, java.lang.String, java.lang.String, java.lang.String> {

	private static final long serialVersionUID = -895166473;

	/**
	 * Setter for <code>collect.ofc_survey_work.id</code>.
	 */
	public void setId(java.lang.Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>collect.ofc_survey_work.id</code>.
	 */
	public java.lang.Integer getId() {
		return (java.lang.Integer) getValue(0);
	}

	/**
	 * Setter for <code>collect.ofc_survey_work.name</code>.
	 */
	public void setName(java.lang.String value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>collect.ofc_survey_work.name</code>.
	 */
	public java.lang.String getName() {
		return (java.lang.String) getValue(1);
	}

	/**
	 * Setter for <code>collect.ofc_survey_work.uri</code>.
	 */
	public void setUri(java.lang.String value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>collect.ofc_survey_work.uri</code>.
	 */
	public java.lang.String getUri() {
		return (java.lang.String) getValue(2);
	}

	/**
	 * Setter for <code>collect.ofc_survey_work.idml</code>.
	 */
	public void setIdml(java.lang.String value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>collect.ofc_survey_work.idml</code>.
	 */
	public java.lang.String getIdml() {
		return (java.lang.String) getValue(3);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Record1<java.lang.Integer> key() {
		return (org.jooq.Record1) super.key();
	}

	// -------------------------------------------------------------------------
	// Record4 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row4<java.lang.Integer, java.lang.String, java.lang.String, java.lang.String> fieldsRow() {
		return (org.jooq.Row4) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row4<java.lang.Integer, java.lang.String, java.lang.String, java.lang.String> valuesRow() {
		return (org.jooq.Row4) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field1() {
		return org.openforis.collect.persistence.jooq.tables.OfcSurveyWork.OFC_SURVEY_WORK.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field2() {
		return org.openforis.collect.persistence.jooq.tables.OfcSurveyWork.OFC_SURVEY_WORK.NAME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field3() {
		return org.openforis.collect.persistence.jooq.tables.OfcSurveyWork.OFC_SURVEY_WORK.URI;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field4() {
		return org.openforis.collect.persistence.jooq.tables.OfcSurveyWork.OFC_SURVEY_WORK.IDML;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value1() {
		return getId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value2() {
		return getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value3() {
		return getUri();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value4() {
		return getIdml();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfcSurveyWorkRecord value1(java.lang.Integer value) {
		setId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfcSurveyWorkRecord value2(java.lang.String value) {
		setName(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfcSurveyWorkRecord value3(java.lang.String value) {
		setUri(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfcSurveyWorkRecord value4(java.lang.String value) {
		setIdml(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfcSurveyWorkRecord values(java.lang.Integer value1, java.lang.String value2, java.lang.String value3, java.lang.String value4) {
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached OfcSurveyWorkRecord
	 */
	public OfcSurveyWorkRecord() {
		super(org.openforis.collect.persistence.jooq.tables.OfcSurveyWork.OFC_SURVEY_WORK);
	}

	/**
	 * Create a detached, initialised OfcSurveyWorkRecord
	 */
	public OfcSurveyWorkRecord(java.lang.Integer id, java.lang.String name, java.lang.String uri, java.lang.String idml) {
		super(org.openforis.collect.persistence.jooq.tables.OfcSurveyWork.OFC_SURVEY_WORK);

		setValue(0, id);
		setValue(1, name);
		setValue(2, uri);
		setValue(3, idml);
	}
}
