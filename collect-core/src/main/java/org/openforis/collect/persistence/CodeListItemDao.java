package org.openforis.collect.persistence;

import static org.openforis.collect.persistence.jooq.Sequences.OFC_CODE_LIST_ID_SEQ;
import static org.openforis.collect.persistence.jooq.tables.OfcCodeList.OFC_CODE_LIST;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Record;
import org.jooq.Result;
import org.jooq.ResultQuery;
import org.jooq.SelectConditionStep;
import org.jooq.SelectQuery;
import org.jooq.StoreQuery;
import org.jooq.TableField;
import org.jooq.impl.Factory;
import org.openforis.collect.model.CollectSurvey;
import org.openforis.collect.persistence.jooq.DialectAwareJooqFactory;
import org.openforis.collect.persistence.jooq.MappingJooqDaoSupport;
import org.openforis.collect.persistence.jooq.MappingJooqFactory;
import org.openforis.collect.persistence.jooq.tables.records.OfcCodeListRecord;
import org.openforis.idm.metamodel.CodeList;
import org.openforis.idm.metamodel.ModelVersion;
import org.openforis.idm.metamodel.PersistedCodeListItem;
import org.openforis.idm.metamodel.Survey;
import org.openforis.idm.metamodel.SurveyObject;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author S. Ricci
 */
@Transactional
public class CodeListItemDao extends MappingJooqDaoSupport<PersistedCodeListItem, CodeListItemDao.JooqFactory> {
	
	@SuppressWarnings("rawtypes")
	private static final TableField[] LABEL_FIELDS = {OFC_CODE_LIST.LABEL1, OFC_CODE_LIST.LABEL2, OFC_CODE_LIST.LABEL3}; 
	@SuppressWarnings("rawtypes")
	private static final TableField[] DESCRIPTION_FIELDS = {OFC_CODE_LIST.DESCRIPTION1, OFC_CODE_LIST.DESCRIPTION2, OFC_CODE_LIST.DESCRIPTION3}; 
	
	public CodeListItemDao() {
		super(CodeListItemDao.JooqFactory.class);
	}

	public PersistedCodeListItem loadById(CodeList list, int id) {
		JooqFactory jf = getMappingJooqFactory(list);
		ResultQuery<?> selectQuery = jf.selectByIdQuery(id);
		Record r = selectQuery.fetchOne();
		if ( r == null ) {
			return null;
		} else {
			return jf.fromRecord(r);
		}
	}

	@Override
	public void insert(PersistedCodeListItem item) {
		JooqFactory jf = getMappingJooqFactory(item.getCodeList());
		jf.insertQuery(item).execute();
	}

	@Override
	public void update(PersistedCodeListItem item) {
		JooqFactory jf = getMappingJooqFactory(item.getCodeList());
		jf.updateQuery(item).execute();
	}
	
	public void shiftItem(PersistedCodeListItem item, int toIndex) {
		CodeList list = item.getCodeList();
		List<PersistedCodeListItem> siblings = loadChildItems(list, item.getParentId()); 
		int newSortOrder;
		int prevItemIdx;
		if ( toIndex >= siblings.size() ) {
			prevItemIdx = siblings.size() - 1;
		} else {
			prevItemIdx = toIndex;
		}
		PersistedCodeListItem previousItem = siblings.get(prevItemIdx);
		newSortOrder = previousItem.getSortOrder();
		updateSortOrder(item, newSortOrder);
	}

	protected void updateSortOrder(PersistedCodeListItem item, int newSortOrder) {
		CodeList list = item.getCodeList();
		CollectSurvey survey = (CollectSurvey) list.getSurvey();
		TableField<OfcCodeListRecord, Integer> surveyIdField = getSurveyIdField(survey.isWork());
		JooqFactory jf = getMappingJooqFactory(list);
		Integer oldSortOrder = item.getSortOrder();
		if ( newSortOrder == oldSortOrder ) {
			return;
		} else {
			//give top sort order to the item
			jf.update(OFC_CODE_LIST)
				.set(OFC_CODE_LIST.SORT_ORDER, 0)
				.where(OFC_CODE_LIST.ID.equal(item.getSystemId())).execute();
			if ( newSortOrder > oldSortOrder ) {
				//move backwards previous items
				jf.update(OFC_CODE_LIST)
					.set(OFC_CODE_LIST.SORT_ORDER, OFC_CODE_LIST.SORT_ORDER.sub(1))
					.where(surveyIdField.equal(survey.getId()),
							OFC_CODE_LIST.CODE_LIST_ID.equal(list.getId()),
							OFC_CODE_LIST.PARENT_ID.equal(item.getParentId()),
							OFC_CODE_LIST.SORT_ORDER.greaterThan(oldSortOrder),
							OFC_CODE_LIST.SORT_ORDER.lessOrEqual(newSortOrder)
							).execute();
			} else {
				//move forward next items
				jf.update(OFC_CODE_LIST)
					.set(OFC_CODE_LIST.SORT_ORDER, OFC_CODE_LIST.SORT_ORDER.add(1))
					.where(surveyIdField.equal(survey.getId()),
							OFC_CODE_LIST.CODE_LIST_ID.equal(list.getId()),
							OFC_CODE_LIST.PARENT_ID.equal(item.getParentId()),
							OFC_CODE_LIST.SORT_ORDER.greaterOrEqual(newSortOrder),
							OFC_CODE_LIST.SORT_ORDER.lessThan(oldSortOrder)
							).execute();
			}
			//set item sort order to final value
			jf.update(OFC_CODE_LIST)
				.set(OFC_CODE_LIST.SORT_ORDER, newSortOrder)
				.where(OFC_CODE_LIST.ID.equal(item.getSystemId())).execute();
		}
	}

	public void delete(int id) {
		JooqFactory jf = getMappingJooqFactory(null);
		jf.deleteQuery(id).execute();
	}

	public void deleteBySurvey(int surveyId) {
		deleteBySurvey(surveyId, false);
	}
	
	public void deleteBySurveyWork(int surveyId) {
		deleteBySurvey(surveyId, true);
	}
	
	public void deleteBySurvey(int surveyId, boolean work) {
		JooqFactory jf = getMappingJooqFactory(null);
		TableField<OfcCodeListRecord, Integer> surveyIdField = getSurveyIdField(work);
		jf.delete(OFC_CODE_LIST)
			.where(surveyIdField.equal(surveyId))
			.execute();
	}

	public void moveToPublishedSurvey(int publishedSurveyId, int surveyWorkId) {
		JooqFactory jf = getMappingJooqFactory(null);
		jf.update(OFC_CODE_LIST)
			.set(OFC_CODE_LIST.SURVEY_ID, publishedSurveyId)
			.set(OFC_CODE_LIST.SURVEY_WORK_ID, (Integer) null)
			.where(OFC_CODE_LIST.SURVEY_WORK_ID.equal(surveyWorkId))
			.execute();
	}

	public List<PersistedCodeListItem> loadRootItems(CodeList codeList) {
		return loadChildItems(codeList, (Integer) null);
	}
	
	public PersistedCodeListItem loadRootItem(CodeList codeList, String code) {
		return loadItem(codeList, (Integer) null, code);
	}
	
	public List<PersistedCodeListItem> loadChildItems(PersistedCodeListItem item) {
		return loadChildItems(item.getCodeList(), item.getSystemId());
	}
	
	protected List<PersistedCodeListItem> loadChildItems(CodeList codeList, Integer parentItemId) {
		JooqFactory jf = getMappingJooqFactory(codeList);
		SelectQuery q = createSelectChildItemsQuery(jf, codeList, parentItemId);
		Result<Record> result = q.fetch();
		return jf.fromResult(result);
	}
	
	public List<PersistedCodeListItem> loadItems(CodeList list, int level) {
		int currentLevel = 1;
		List<PersistedCodeListItem> currentLevelItems = loadRootItems(list);;
		List<PersistedCodeListItem> nextLevelItems;
		while ( currentLevel < level ) {
			nextLevelItems = new ArrayList<PersistedCodeListItem>();
			for (PersistedCodeListItem item : currentLevelItems) {
				List<PersistedCodeListItem> childItems = loadChildItems(item);
				nextLevelItems.addAll(childItems);
			}
			currentLevelItems = nextLevelItems;
			currentLevel++;
		}
		return currentLevelItems;
	}
	
	public boolean hasChildItems(CodeList codeList, Integer parentItemId) {
		JooqFactory jf = getMappingJooqFactory(codeList);
		SelectQuery q = createSelectChildItemsQuery(jf, codeList, parentItemId);
		q.addSelect(Factory.count());
		Record record = q.fetchOne();
		Integer count = record.getValueAsInteger(0);
		return count > 0;
	}
	
	public boolean hasQualifiableItems(CodeList codeList) {
		JooqFactory jf = getMappingJooqFactory(codeList);
		CollectSurvey survey = (CollectSurvey) codeList.getSurvey();
		TableField<OfcCodeListRecord, Integer> surveyIdField = getSurveyIdField(survey.isWork());
		SelectConditionStep q = jf.selectCount()
				.from(OFC_CODE_LIST)
				.where(
					surveyIdField.equal(survey.getId()),
					OFC_CODE_LIST.CODE_LIST_ID.equal(codeList.getId()),
					OFC_CODE_LIST.QUALIFIABLE.equal(Boolean.TRUE)
				);
		Record r = q.fetchOne();
		if ( r == null ) {
			return false;
		} else {
			Integer count = r.getValueAsInteger(0);
			return count > 0;
		}
	}
	
	public PersistedCodeListItem loadItem(CodeList codeList, Integer parentItemId, String code) {
		JooqFactory jf = getMappingJooqFactory(codeList);
		SelectQuery q = createSelectChildItemsQuery(jf, codeList, parentItemId);
		q.addConditions(
				OFC_CODE_LIST.CODE.equal(code)
		);
		Record r = q.fetchOne();
		if ( r == null ) {
			return null;
		} else {
			return jf.fromRecord(r);
		}
	}

	protected static SelectQuery createSelectChildItemsQuery(JooqFactory jf, CodeList codeList, Integer parentItemId) {
		SelectQuery q = jf.selectQuery();	
		q.addFrom(OFC_CODE_LIST);
		addFilterByParentItemConditions(q, codeList, parentItemId);
		q.addOrderBy(OFC_CODE_LIST.SORT_ORDER);
		return q;
	}

	protected static void addFilterByParentItemConditions(SelectQuery select,
			CodeList codeList, Integer parentItemId) {
		CollectSurvey survey = (CollectSurvey) codeList.getSurvey();
		TableField<OfcCodeListRecord, Integer> surveyIdField = getSurveyIdField(survey.isWork());
		select.addConditions(
				surveyIdField.equal(survey.getId()),
				OFC_CODE_LIST.CODE_LIST_ID.equal(codeList.getId()),
				OFC_CODE_LIST.PARENT_ID.equal(parentItemId));
	}
	
	@Override
	protected JooqFactory getMappingJooqFactory() {
		throw new UnsupportedOperationException();
	}
	
	protected JooqFactory getMappingJooqFactory(CodeList codeList) {
		Connection connection = getConnection();
		return new JooqFactory(connection, codeList);
	}
	
	@Override
	protected DialectAwareJooqFactory getJooqFactory() {
		throw new UnsupportedOperationException();
	}
	
	protected DialectAwareJooqFactory getJooqFactory(CodeList codeList) {
		Connection connection = getConnection();
		return new JooqFactory(connection, codeList);
	}
	
	protected static TableField<OfcCodeListRecord, Integer> getSurveyIdField(
			boolean work) {
		TableField<OfcCodeListRecord, Integer> surveyIdField = work ? 
				OFC_CODE_LIST.SURVEY_WORK_ID: OFC_CODE_LIST.SURVEY_ID;
		return surveyIdField;
	}
	
	protected static class JooqFactory extends MappingJooqFactory<PersistedCodeListItem> {

		private static final long serialVersionUID = 1L;
		
		private CodeList codeList;
		
		public JooqFactory(Connection connection, CodeList codeList) {
			super(connection, OFC_CODE_LIST.ID, OFC_CODE_LIST_ID_SEQ, PersistedCodeListItem.class);
			this.codeList = codeList;
		}
		
		@Override
		protected PersistedCodeListItem newEntity() {
			throw new UnsupportedOperationException();
		}
		
		protected PersistedCodeListItem newEntity(int itemId) {
			return new PersistedCodeListItem(codeList, itemId);
		}
		
		@Override
		public PersistedCodeListItem fromRecord(Record record) {
			int itemId = record.getValue(OFC_CODE_LIST.ITEM_ID);
			PersistedCodeListItem entity = newEntity(itemId);
			fromRecord(record, entity);
			return entity;
		}

		@Override
		public void fromRecord(Record r, PersistedCodeListItem i) {
			i.setSystemId(r.getValue(OFC_CODE_LIST.ID));
			i.setSortOrder(r.getValue(OFC_CODE_LIST.SORT_ORDER));
			i.setCode(r.getValue(OFC_CODE_LIST.CODE));
			i.setParentId(r.getValue(OFC_CODE_LIST.PARENT_ID));
			i.setQualifiable(r.getValue(OFC_CODE_LIST.QUALIFIABLE));
			i.setSinceVersion(extractModelVersion(r, i, OFC_CODE_LIST.SINCE_VERSION_ID));
			i.setDeprecatedVersion(extractModelVersion(r, i, OFC_CODE_LIST.DEPRECATED_VERSION_ID));
			extractLabels(r, i);
			extractDescriptions(r, i);
		}

		protected ModelVersion extractModelVersion(Record r,
				SurveyObject surveyObject,
				TableField<OfcCodeListRecord, Integer> versionIdField) {
			Survey survey = surveyObject.getSurvey();
			Integer versionId = r.getValue(versionIdField);
			ModelVersion version = versionId == null ? null: survey.getVersionById(versionId);
			return version;
		}
		
		protected void extractLabels(Record r, PersistedCodeListItem item) {
			Survey survey = codeList.getSurvey();
			item.removeAllLabels();
			List<String> languages = survey.getLanguages();
			for (int i = 0; i < languages.size(); i++) {
				String lang = languages.get(i);
				String label = r.getValueAsString(LABEL_FIELDS[i]);
				item.setLabel(lang, label);
			}
		}
		
		protected void extractDescriptions(Record r, PersistedCodeListItem item) {
			Survey survey = codeList.getSurvey();
			item.removeAllDescriptions();
			List<String> languages = survey.getLanguages();
			for (int i = 0; i < languages.size(); i++) {
				String lang = languages.get(i);
				String label = r.getValueAsString(DESCRIPTION_FIELDS[i]);
				item.setDescription(lang, label);
			}
		}

		@Override
		public void fromObject(PersistedCodeListItem item, StoreQuery<?> q) {
			q.addValue(OFC_CODE_LIST.ID, item.getSystemId());
			CollectSurvey survey = (CollectSurvey) item.getSurvey();
			Integer surveyId = survey.getId();
			if ( survey.isWork() ) {
				q.addValue(OFC_CODE_LIST.SURVEY_ID, (Integer) null);
				q.addValue(OFC_CODE_LIST.SURVEY_WORK_ID, surveyId);
			} else {
				q.addValue(OFC_CODE_LIST.SURVEY_ID, surveyId);
				q.addValue(OFC_CODE_LIST.SURVEY_WORK_ID, (Integer) null);
			}
			q.addValue(OFC_CODE_LIST.CODE_LIST_ID, item.getCodeList().getId());
			q.addValue(OFC_CODE_LIST.ITEM_ID, item.getId());
			q.addValue(OFC_CODE_LIST.PARENT_ID, item.getParentId());
			Integer sortOrder = item.getSortOrder();
			if ( sortOrder == null ) {
				sortOrder = nextSortOrder(item);
			}
			q.addValue(OFC_CODE_LIST.SORT_ORDER, sortOrder);
			q.addValue(OFC_CODE_LIST.CODE, item.getCode());
			q.addValue(OFC_CODE_LIST.QUALIFIABLE, item.isQualifiable());
			Integer sinceVersionId = item.getSinceVersion() == null ? null: item.getSinceVersion().getId();
			q.addValue(OFC_CODE_LIST.SINCE_VERSION_ID, sinceVersionId);
			Integer deprecatedVersionId = item.getDeprecatedVersion() == null ? null: item.getDeprecatedVersion().getId();
			q.addValue(OFC_CODE_LIST.DEPRECATED_VERSION_ID, deprecatedVersionId);
			addLabelValues(q, item);
			addDescriptionValues(q, item);
		}

		private Integer nextSortOrder(PersistedCodeListItem item) {
			SelectQuery select = selectQuery();
			select.addSelect(max(OFC_CODE_LIST.SORT_ORDER));
			select.addFrom(OFC_CODE_LIST);
			addFilterByParentItemConditions(select, codeList, item.getParentId());
			Record record = select.fetchOne();
			Integer max = record.getValueAsInteger(0);
			return max == null ? 1: max + 1;
		}

		protected void addLabelValues(StoreQuery<?> q, PersistedCodeListItem item) {
			Survey survey = item.getSurvey();
			List<String> languages = survey.getLanguages();
			for (int i = 0; i < LABEL_FIELDS.length; i++) {
				@SuppressWarnings("unchecked")
				TableField<?, String> field = LABEL_FIELDS[i];
				String label;
				if ( i < languages.size() ) {
					String lang = languages.get(i);
					label = getLabel(item, lang);
				} else {
					label = null;
				}
				q.addValue(field, label);
			}
		}

		protected String getLabel(PersistedCodeListItem item, String lang) {
			String label = item.getLabel(lang);
			if ( label == null ) {
				CollectSurvey survey = (CollectSurvey) item.getSurvey();
				if ( survey.isDefaultLanguage(lang) ) {
					label = item.getLabel(null);
				}
			}
			return label;
		}
		
		protected void addDescriptionValues(StoreQuery<?> q, PersistedCodeListItem item) {
			Survey survey = item.getSurvey();
			List<String> languages = survey.getLanguages();
			for (int i = 0; i < DESCRIPTION_FIELDS.length; i++) {
				@SuppressWarnings("unchecked")
				TableField<?, String> field = DESCRIPTION_FIELDS[i];
				String description;
				if ( i < languages.size() ) {
					String lang = languages.get(i);
					description = getDescription(item, lang);
				} else {
					description = null;
				}
				q.addValue(field, description);
			}
		}

		protected String getDescription(PersistedCodeListItem item, String lang) {
			String description = item.getDescription(lang);
			if ( description == null ) {
				CollectSurvey survey = (CollectSurvey) item.getSurvey();
				if ( survey.isDefaultLanguage(lang) ) {
					description = item.getDescription(null);
				}
			}
			return description;
		}
		
		@Override
		protected void setId(PersistedCodeListItem t, int id) {
			t.setSystemId(id);
		}

		@Override
		protected Integer getId(PersistedCodeListItem t) {
			return t.getSystemId();
		}
		
	}

}
