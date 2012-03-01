/**
 * This class is generated by jOOQ
 */
package org.openforis.collect.persistence.jooq.tables;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = {"http://www.jooq.org", "2.0.1"},
                            comments = "This class is generated by jOOQ")
public class OfcEntity extends org.jooq.impl.UpdatableTableImpl<org.openforis.collect.persistence.jooq.tables.records.OfcEntityRecord> {

	private static final long serialVersionUID = -556425291;

	/**
	 * The singleton instance of ofc_entity
	 */
	public static final org.openforis.collect.persistence.jooq.tables.OfcEntity OFC_ENTITY = new org.openforis.collect.persistence.jooq.tables.OfcEntity();

	/**
	 * The class holding records for this type
	 */
	private static final java.lang.Class<org.openforis.collect.persistence.jooq.tables.records.OfcEntityRecord> __RECORD_TYPE = org.openforis.collect.persistence.jooq.tables.records.OfcEntityRecord.class;

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.openforis.collect.persistence.jooq.tables.records.OfcEntityRecord> getRecordType() {
		return __RECORD_TYPE;
	}

	/**
	 * An uncommented item
	 * 
	 * PRIMARY KEY
	 */
	public final org.jooq.TableField<org.openforis.collect.persistence.jooq.tables.records.OfcEntityRecord, java.lang.Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * An uncommented item
	 * <p>
	 * <code><pre>
	 * FOREIGN KEY [collect.ofc_entity.record_id]
	 * REFERENCES ofc_record [collect.ofc_record.id]
	 * </pre></code>
	 */
	public final org.jooq.TableField<org.openforis.collect.persistence.jooq.tables.records.OfcEntityRecord, java.lang.Integer> RECORD_ID = createField("record_id", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.openforis.collect.persistence.jooq.tables.records.OfcEntityRecord, java.lang.Integer> PARENT_ID = createField("parent_id", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * An uncommented item
	 * <p>
	 * <code><pre>
	 * FOREIGN KEY [collect.ofc_entity.definition_id]
	 * REFERENCES ofc_schema_definition [collect.ofc_schema_definition.id]
	 * </pre></code>
	 */
	public final org.jooq.TableField<org.openforis.collect.persistence.jooq.tables.records.OfcEntityRecord, java.lang.Integer> DEFINITION_ID = createField("definition_id", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.openforis.collect.persistence.jooq.tables.records.OfcEntityRecord, java.lang.Integer> POSITION = createField("position", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * No further instances allowed
	 */
	private OfcEntity() {
		super("ofc_entity", org.openforis.collect.persistence.jooq.Collect.COLLECT);
	}

	/**
	 * No further instances allowed
	 */
	private OfcEntity(java.lang.String alias) {
		super(alias, org.openforis.collect.persistence.jooq.Collect.COLLECT, org.openforis.collect.persistence.jooq.tables.OfcEntity.OFC_ENTITY);
	}

	@Override
	public org.jooq.UniqueKey<org.openforis.collect.persistence.jooq.tables.records.OfcEntityRecord> getMainKey() {
		return org.openforis.collect.persistence.jooq.Keys.ofc_entity_pkey;
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.List<org.jooq.UniqueKey<org.openforis.collect.persistence.jooq.tables.records.OfcEntityRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<org.openforis.collect.persistence.jooq.tables.records.OfcEntityRecord>>asList(org.openforis.collect.persistence.jooq.Keys.ofc_entity_pkey);
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.List<org.jooq.ForeignKey<org.openforis.collect.persistence.jooq.tables.records.OfcEntityRecord, ?>> getReferences() {
		return java.util.Arrays.<org.jooq.ForeignKey<org.openforis.collect.persistence.jooq.tables.records.OfcEntityRecord, ?>>asList(org.openforis.collect.persistence.jooq.Keys.ofc_entity__ofc_entity_record_fkey, org.openforis.collect.persistence.jooq.Keys.ofc_entity__ofc_entity_definition_fkey);
	}

	@Override
	public org.openforis.collect.persistence.jooq.tables.OfcEntity as(java.lang.String alias) {
		return new org.openforis.collect.persistence.jooq.tables.OfcEntity(alias);
	}
}
