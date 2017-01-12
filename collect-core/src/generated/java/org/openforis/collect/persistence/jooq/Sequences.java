/**
 * This class is generated by jOOQ
 */
package org.openforis.collect.persistence.jooq;


import javax.annotation.Generated;

import org.jooq.Sequence;
import org.jooq.impl.SequenceImpl;
import org.openforis.collect.persistence.jooq.Collect;


/**
 * Convenience access to all sequences in collect
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.6.2"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Sequences {

	/**
	 * The sequence <code>collect.ofc_code_list_id_seq</code>
	 */
	public static final Sequence<Long> OFC_CODE_LIST_ID_SEQ = new SequenceImpl<Long>("ofc_code_list_id_seq", Collect.COLLECT, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

	/**
	 * The sequence <code>collect.ofc_data_cleansing_chain_id_seq</code>
	 */
	public static final Sequence<Long> OFC_DATA_CLEANSING_CHAIN_ID_SEQ = new SequenceImpl<Long>("ofc_data_cleansing_chain_id_seq", Collect.COLLECT, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

	/**
	 * The sequence <code>collect.ofc_data_cleansing_report_id_seq</code>
	 */
	public static final Sequence<Long> OFC_DATA_CLEANSING_REPORT_ID_SEQ = new SequenceImpl<Long>("ofc_data_cleansing_report_id_seq", Collect.COLLECT, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

	/**
	 * The sequence <code>collect.ofc_data_cleansing_step_id_seq</code>
	 */
	public static final Sequence<Long> OFC_DATA_CLEANSING_STEP_ID_SEQ = new SequenceImpl<Long>("ofc_data_cleansing_step_id_seq", Collect.COLLECT, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

	/**
	 * The sequence <code>collect.ofc_data_query_group_id_seq</code>
	 */
	public static final Sequence<Long> OFC_DATA_QUERY_GROUP_ID_SEQ = new SequenceImpl<Long>("ofc_data_query_group_id_seq", Collect.COLLECT, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

	/**
	 * The sequence <code>collect.ofc_data_query_id_seq</code>
	 */
	public static final Sequence<Long> OFC_DATA_QUERY_ID_SEQ = new SequenceImpl<Long>("ofc_data_query_id_seq", Collect.COLLECT, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

	/**
	 * The sequence <code>collect.ofc_data_query_type_id_seq</code>
	 */
	public static final Sequence<Long> OFC_DATA_QUERY_TYPE_ID_SEQ = new SequenceImpl<Long>("ofc_data_query_type_id_seq", Collect.COLLECT, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

	/**
	 * The sequence <code>collect.ofc_data_report_id_seq</code>
	 */
	public static final Sequence<Long> OFC_DATA_REPORT_ID_SEQ = new SequenceImpl<Long>("ofc_data_report_id_seq", Collect.COLLECT, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

	/**
	 * The sequence <code>collect.ofc_data_report_item_id_seq</code>
	 */
	public static final Sequence<Long> OFC_DATA_REPORT_ITEM_ID_SEQ = new SequenceImpl<Long>("ofc_data_report_item_id_seq", Collect.COLLECT, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

	/**
	 * The sequence <code>collect.ofc_institution_id_seq</code>
	 */
	public static final Sequence<Long> OFC_INSTITUTION_ID_SEQ = new SequenceImpl<Long>("ofc_institution_id_seq", Collect.COLLECT, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

	/**
	 * The sequence <code>collect.ofc_message_sequence_no_seq</code>
	 */
	public static final Sequence<Long> OFC_MESSAGE_SEQUENCE_NO_SEQ = new SequenceImpl<Long>("ofc_message_sequence_no_seq", Collect.COLLECT, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

	/**
	 * The sequence <code>collect.ofc_record_id_seq</code>
	 */
	public static final Sequence<Long> OFC_RECORD_ID_SEQ = new SequenceImpl<Long>("ofc_record_id_seq", Collect.COLLECT, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

	/**
	 * The sequence <code>collect.ofc_sampling_design_id_seq</code>
	 */
	public static final Sequence<Long> OFC_SAMPLING_DESIGN_ID_SEQ = new SequenceImpl<Long>("ofc_sampling_design_id_seq", Collect.COLLECT, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

	/**
	 * The sequence <code>collect.ofc_survey_file_id_seq</code>
	 */
	public static final Sequence<Long> OFC_SURVEY_FILE_ID_SEQ = new SequenceImpl<Long>("ofc_survey_file_id_seq", Collect.COLLECT, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

	/**
	 * The sequence <code>collect.ofc_survey_id_seq</code>
	 */
	public static final Sequence<Long> OFC_SURVEY_ID_SEQ = new SequenceImpl<Long>("ofc_survey_id_seq", Collect.COLLECT, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

	/**
	 * The sequence <code>collect.ofc_taxon_id_seq</code>
	 */
	public static final Sequence<Long> OFC_TAXON_ID_SEQ = new SequenceImpl<Long>("ofc_taxon_id_seq", Collect.COLLECT, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

	/**
	 * The sequence <code>collect.ofc_taxon_vernacular_name_id_seq</code>
	 */
	public static final Sequence<Long> OFC_TAXON_VERNACULAR_NAME_ID_SEQ = new SequenceImpl<Long>("ofc_taxon_vernacular_name_id_seq", Collect.COLLECT, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

	/**
	 * The sequence <code>collect.ofc_taxonomy_id_seq</code>
	 */
	public static final Sequence<Long> OFC_TAXONOMY_ID_SEQ = new SequenceImpl<Long>("ofc_taxonomy_id_seq", Collect.COLLECT, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

	/**
	 * The sequence <code>collect.ofc_user_id_seq</code>
	 */
	public static final Sequence<Long> OFC_USER_ID_SEQ = new SequenceImpl<Long>("ofc_user_id_seq", Collect.COLLECT, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

	/**
	 * The sequence <code>collect.ofc_user_role_id_seq</code>
	 */
	public static final Sequence<Long> OFC_USER_ROLE_ID_SEQ = new SequenceImpl<Long>("ofc_user_role_id_seq", Collect.COLLECT, org.jooq.impl.SQLDataType.BIGINT.nullable(false));
}
