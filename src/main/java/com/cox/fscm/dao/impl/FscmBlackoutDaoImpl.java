package com.cox.fscm.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import com.cox.fscm.common.Constants;
import com.cox.fscm.common.Utility;
import com.cox.fscm.dao.FscmBlackoutDao;
import com.cox.fscm.dto.FscmBlackoutModel;
import com.cox.fscm.exception.AdminGenericException;

@Repository("blackoutRepository")
public class FscmBlackoutDaoImpl implements FscmBlackoutDao {

	@Autowired
	private SessionFactory sessionFactory;

	private static final Logger LOGGER = LogManager.getLogger(Constants.FSCMADMINBLACKOUT);

	@SuppressWarnings("unchecked")
	@Override
	public List<FscmBlackoutModel> getBlackoutDates(String transactionId) throws AdminGenericException {
		long startTime=System.currentTimeMillis();
		try {
			return getSession().createCriteria(FscmBlackoutModel.class).list();
		} catch (Exception e) {
			LOGGER.error(
					Utility.logErrorMessage(transactionId, "Exception happened while getting black out dates from DB."), e);
			throw new AdminGenericException(e.getMessage());
		}
		finally {
			LOGGER.info(Utility.logInfoMessage(transactionId, Constants.TRANSACTION_TIME_DB,
					(System.currentTimeMillis() - startTime)));
		}
	}

	@Override
	public List<Integer> setBlackoutDates(List<FscmBlackoutModel> models, String transactionId)
			throws AdminGenericException {
		long startTime=System.currentTimeMillis();
		try {
			LOGGER.info(
					Utility.logInfoMessage(transactionId, " Number of rows to insert/update to DB:", models.size()));
			List<Integer> newBlackoutIds = new ArrayList<Integer>();
			for (FscmBlackoutModel model : models) {
				StringBuffer infoMessage = new StringBuffer();
				if (model != null) {
					infoMessage.append("Setting Blackout date for Region:").append(model.getRegion())
							.append(" on date: ").append(model.getStartDate());
				}
				LOGGER.info(Utility.logInfoMessage(transactionId, infoMessage.toString()));
				if (null != model.getId()) {
					FscmBlackoutModel liveModel = (FscmBlackoutModel) getSession().get(FscmBlackoutModel.class,
							model.getId());
					updateLiveModel(model, liveModel, getSession(), transactionId);
				} else {
					model.setCreateDate(new Date());
					getSession().save(model);
					LOGGER.info(Utility.logInfoMessage(transactionId, "Inserted Blackout date. Id of new blackout is:"
							+ ((model != null) ? model.getId() : model)));
				}
			}
			return newBlackoutIds;
		} catch (Exception e) {
			LOGGER.error(Utility.logErrorMessage(null, "Exception happened while setting black out dates from DB."), e);
			throw new AdminGenericException(e.getMessage());
		}
		finally {
			LOGGER.info(Utility.logInfoMessage(transactionId, Constants.TRANSACTION_TIME_DB,
					(System.currentTimeMillis() - startTime)));
		}
	}

	private void updateLiveModel(FscmBlackoutModel model, FscmBlackoutModel liveModel, Session session,
			String transactionId) throws AdminGenericException {
		try{
		if (null != liveModel) {
			liveModel.setStartDate(model.getStartDate());
			liveModel.setEndDate(model.getEndDate());
			liveModel.setModifiedBy(Constants.FSCM_USER);
			liveModel.setModifiedDate(new Date());
			liveModel.setOpenDate(model.getOpenDate());
			liveModel.setRegion(model.getRegion());
			if (null != model.getBlackoutName()) {
				liveModel.setBlackoutName(model.getBlackoutName());
			}
			session.update(liveModel);
			StringBuffer infoMessage = new StringBuffer();
			infoMessage.append("Updated Blackout date for Region:").append(model.getRegion()).append(" on date: ")
					.append(model.getStartDate());
			LOGGER.info(Utility.logInfoMessage(transactionId, infoMessage.toString()));
		} else {
			throw new AdminGenericException("Couldn't find the specified row in the Database.");
		}
		}
		catch (Exception e) {
			throw new AdminGenericException("Error while updating model for Updated Blackout date for Region ");
		}
	}

	@Override
	public void removeBlackoutDates(List<FscmBlackoutModel> models, String transactionId) throws AdminGenericException {
		long startTime=System.currentTimeMillis();
		try {
			LOGGER.info(Utility.logInfoMessage(transactionId, " Deleting blackout Dates"));
			for (FscmBlackoutModel fscmBlackoutModel : models) {
				if (null != fscmBlackoutModel.getId()) {
					getSession().delete(fscmBlackoutModel);
				} else {
					String hql = "delete from FscmBlackoutModel where region= :region " + "AND blackoutName = :bName "
							+ "AND openDate= :oDate ";
					getSession().createQuery(hql).setString("region", fscmBlackoutModel.getRegion())
							.setString("bName", fscmBlackoutModel.getBlackoutName())
							.setDate("oDate", fscmBlackoutModel.getOpenDate()).executeUpdate();
				}
			}
		} catch (Exception e) {
			LOGGER.error(
					Utility.logErrorMessage(transactionId, "Exception happened while removing black out dates from DB."),
					e);
			throw new AdminGenericException(e.getMessage());
		}
		finally {			
			LOGGER.info(Utility.logInfoMessage(transactionId, Constants.TRANSACTION_TIME_DB,
					(System.currentTimeMillis() - startTime)));
		}
	}

	private Session getSession() {
		if (sessionFactory.getCurrentSession() != null) {
			return sessionFactory.getCurrentSession();
		} else {
			return sessionFactory.openSession();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FscmBlackoutModel> fetchBlackoutDates(String exceptionRegion, Date exceptionStartDate,
			Date exceptionEndDate, String transactionId) throws AdminGenericException {
		long startTime=System.currentTimeMillis();
		try {
		/*
		 * Check Exception start/end is a part of Blackout start/end
		 */
		String hql = "FROM FscmBlackoutModel WHERE " + "region = :region AND ("
				+ "startDate BETWEEN :exceptionStartDate AND :exceptionEndDate "
				+ "OR endDate BETWEEN :exceptionStartDate AND :exceptionEndDate "
				+ "OR (startDate < :exceptionStartDate AND endDate > :exceptionStartDate ))";

		List<FscmBlackoutModel> blackouts = getSession().createQuery(hql)
				.setDate("exceptionStartDate", exceptionStartDate).setDate("exceptionEndDate", exceptionEndDate)
				.setString("region", exceptionRegion).list();
		LOGGER.info(Utility.logInfoMessage(transactionId, "Number of rows to fetched from DB: ",
				CollectionUtils.isEmpty(blackouts)?blackouts.size():StringUtils.EMPTY));		
		return blackouts;
		}
		catch (Exception e) {
			LOGGER.error(
					Utility.logErrorMessage(transactionId, "Exception happened while fetching black out dates from DB."),
					e);
			throw new AdminGenericException(e.getMessage());
		}
		finally {			
			LOGGER.info(Utility.logInfoMessage(transactionId, Constants.TRANSACTION_TIME_DB,
					(System.currentTimeMillis() - startTime)));
		}		
	}
}
