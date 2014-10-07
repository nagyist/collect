/**
 * This class is generated by jOOQ
 */
package org.openforis.collect.persistence.jooq.tables;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = { "http://www.jooq.org", "3.4.2" },
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class OfcTaxonVernacularName extends org.jooq.impl.TableImpl<org.openforis.collect.persistence.jooq.tables.records.OfcTaxonVernacularNameRecord> {

	private static final long serialVersionUID = -1876382769;

	/**
	 * The singleton instance of <code>collect.ofc_taxon_vernacular_name</code>
	 */
	public static final org.openforis.collect.persistence.jooq.tables.OfcTaxonVernacularName OFC_TAXON_VERNACULAR_NAME = new org.openforis.collect.persistence.jooq.tables.OfcTaxonVernacularName();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.openforis.collect.persistence.jooq.tables.records.OfcTaxonVernacularNameRecord> getRecordType() {
		return org.openforis.collect.persistence.jooq.tables.records.OfcTaxonVernacularNameRecord.class;
	}

	/**
	 * The column <code>collect.ofc_taxon_vernacular_name.id</code>.
	 */
	public final org.jooq.TableField<org.openforis.collect.persistence.jooq.tables.records.OfcTaxonVernacularNameRecord, java.lang.Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>collect.ofc_taxon_vernacular_name.vernacular_name</code>.
	 */
	public final org.jooq.TableField<org.openforis.collect.persistence.jooq.tables.records.OfcTaxonVernacularNameRecord, java.lang.String> VERNACULAR_NAME = createField("vernacular_name", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "");

	/**
	 * The column <code>collect.ofc_taxon_vernacular_name.language_code</code>.
	 */
	public final org.jooq.TableField<org.openforis.collect.persistence.jooq.tables.records.OfcTaxonVernacularNameRecord, java.lang.String> LANGUAGE_CODE = createField("language_code", org.jooq.impl.SQLDataType.VARCHAR.length(3).nullable(false), this, "");

	/**
	 * The column <code>collect.ofc_taxon_vernacular_name.language_variety</code>.
	 */
	public final org.jooq.TableField<org.openforis.collect.persistence.jooq.tables.records.OfcTaxonVernacularNameRecord, java.lang.String> LANGUAGE_VARIETY = createField("language_variety", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "");

	/**
	 * The column <code>collect.ofc_taxon_vernacular_name.taxon_id</code>.
	 */
	public final org.jooq.TableField<org.openforis.collect.persistence.jooq.tables.records.OfcTaxonVernacularNameRecord, java.lang.Integer> TAXON_ID = createField("taxon_id", org.jooq.impl.SQLDataType.INTEGER, this, "");

	/**
	 * The column <code>collect.ofc_taxon_vernacular_name.step</code>.
	 */
	public final org.jooq.TableField<org.openforis.collect.persistence.jooq.tables.records.OfcTaxonVernacularNameRecord, java.lang.Integer> STEP = createField("step", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>collect.ofc_taxon_vernacular_name.qualifier1</code>.
	 */
	public final org.jooq.TableField<org.openforis.collect.persistence.jooq.tables.records.OfcTaxonVernacularNameRecord, java.lang.String> QUALIFIER1 = createField("qualifier1", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "");

	/**
	 * The column <code>collect.ofc_taxon_vernacular_name.qualifier2</code>.
	 */
	public final org.jooq.TableField<org.openforis.collect.persistence.jooq.tables.records.OfcTaxonVernacularNameRecord, java.lang.String> QUALIFIER2 = createField("qualifier2", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "");

	/**
	 * The column <code>collect.ofc_taxon_vernacular_name.qualifier3</code>.
	 */
	public final org.jooq.TableField<org.openforis.collect.persistence.jooq.tables.records.OfcTaxonVernacularNameRecord, java.lang.String> QUALIFIER3 = createField("qualifier3", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "");

	/**
	 * Create a <code>collect.ofc_taxon_vernacular_name</code> table reference
	 */
	public OfcTaxonVernacularName() {
		this("ofc_taxon_vernacular_name", null);
	}

	/**
	 * Create an aliased <code>collect.ofc_taxon_vernacular_name</code> table reference
	 */
	public OfcTaxonVernacularName(java.lang.String alias) {
		this(alias, org.openforis.collect.persistence.jooq.tables.OfcTaxonVernacularName.OFC_TAXON_VERNACULAR_NAME);
	}

	private OfcTaxonVernacularName(java.lang.String alias, org.jooq.Table<org.openforis.collect.persistence.jooq.tables.records.OfcTaxonVernacularNameRecord> aliased) {
		this(alias, aliased, null);
	}

	private OfcTaxonVernacularName(java.lang.String alias, org.jooq.Table<org.openforis.collect.persistence.jooq.tables.records.OfcTaxonVernacularNameRecord> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, org.openforis.collect.persistence.jooq.Collect.COLLECT, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<org.openforis.collect.persistence.jooq.tables.records.OfcTaxonVernacularNameRecord> getPrimaryKey() {
		return org.openforis.collect.persistence.jooq.Keys.OFC_TAXON_VERNACULAR_NAME_PKEY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<org.openforis.collect.persistence.jooq.tables.records.OfcTaxonVernacularNameRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<org.openforis.collect.persistence.jooq.tables.records.OfcTaxonVernacularNameRecord>>asList(org.openforis.collect.persistence.jooq.Keys.OFC_TAXON_VERNACULAR_NAME_PKEY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.ForeignKey<org.openforis.collect.persistence.jooq.tables.records.OfcTaxonVernacularNameRecord, ?>> getReferences() {
		return java.util.Arrays.<org.jooq.ForeignKey<org.openforis.collect.persistence.jooq.tables.records.OfcTaxonVernacularNameRecord, ?>>asList(org.openforis.collect.persistence.jooq.Keys.OFC_TAXON_VERNACULAR_NAME__OFC_TAXON_VERNACULAR_NAME_TAXON_FKEY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.openforis.collect.persistence.jooq.tables.OfcTaxonVernacularName as(java.lang.String alias) {
		return new org.openforis.collect.persistence.jooq.tables.OfcTaxonVernacularName(alias, this);
	}

	/**
	 * Rename this table
	 */
	public org.openforis.collect.persistence.jooq.tables.OfcTaxonVernacularName rename(java.lang.String name) {
		return new org.openforis.collect.persistence.jooq.tables.OfcTaxonVernacularName(name, null);
	}
}
