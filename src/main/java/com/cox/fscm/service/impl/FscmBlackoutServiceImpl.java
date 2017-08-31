package com.cox.fscm.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cox.fscm.commontypes.FscmBlackout;
import com.cox.fscm.dao.FscmBlackoutDao;
import com.cox.fscm.dto.FscmBlackoutModel;
import com.cox.fscm.exception.AdminGenericException;
import com.cox.fscm.service.FscmBlackoutService;

@Service("blackoutService")
@Transactional
public class FscmBlackoutServiceImpl implements FscmBlackoutService {

	@Autowired
	private FscmBlackoutDao dao;

	private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("MM/dd/yyyy");

	@Override
	public List<FscmBlackout> getBlackoutDates(String transactionId) throws AdminGenericException {
		List<FscmBlackoutModel> blackoutModels = dao.getBlackoutDates(transactionId);
		List<FscmBlackout> records = new ArrayList<FscmBlackout>();
		for (FscmBlackoutModel model : blackoutModels) {
			FscmBlackout record = new FscmBlackout();

			record.setId(model.getId());
			record.setRegion(model.getRegion());
			record.setCreatedBy(model.getCreatedBy());
			record.setModifiedBy(model.getModifiedBy());

			if (null != model.getBlackoutName()) {
				record.setBlackoutName(model.getBlackoutName());
			}
			if (null != model.getOpenDate()) {
				String openDate = FORMATTER.format(model.getOpenDate());
				record.setOpenDate(openDate);
			}

			if (null != model.getStartDate()) {
				String startDate = FORMATTER.format(model.getStartDate());
				record.setStartDate(startDate);
			}

			if (null != model.getEndDate()) {
				String endDate = FORMATTER.format(model.getEndDate());
				record.setEndDate(endDate);
			}

			if (null != model.getCreateDate()) {
				String createDate = FORMATTER.format(model.getCreateDate());
				record.setCreateDate(createDate);
			}
			if (null != model.getModifiedDate()) {
				String modifiedDate = FORMATTER.format(model.getModifiedDate());
				record.setModifiedDate(modifiedDate);
			}
			records.add(record);
		}
		return records;
	}

	@Override
	public List<Integer> setBlackoutDates(List<FscmBlackout> blackoutRecords,String transactionId)
			throws AdminGenericException {
		List<FscmBlackoutModel> blackoutModels = new ArrayList<FscmBlackoutModel>();
		List<FscmBlackout> getAllBlackOuts = getBlackoutDates(transactionId);
		for (FscmBlackout record : blackoutRecords) {
			FscmBlackoutModel model = new FscmBlackoutModel();
			try {
				model.setId(record.getId());
				model.setRegion(record.getRegion());
				model.setCreatedBy(record.getCreatedBy());
				model.setModifiedBy(record.getModifiedBy());

				if (null != record.getBlackoutName()) {
					model.setBlackoutName(record.getBlackoutName());
				} else {
					throw new AdminGenericException("Missing Blackout name in request");
				}
				Date startDate = null;
				if (null != record.getStartDate() && null != record.getEndDate()) {
					startDate = FORMATTER.parse(record.getStartDate());
					Date endDate = FORMATTER.parse(record.getEndDate());
					if (isValidDateOrder(startDate, endDate)) {

						model.setStartDate(startDate);
						model.setEndDate(endDate);
					} else {
						throw new AdminGenericException("Start Date can't be after End date");
					}
				} else {
					throw new AdminGenericException("Missing Start or End date in request");
				}

				if (null != record.getCreateDate()) {
					Date createDate = FORMATTER.parse(record.getCreateDate());
					model.setCreateDate(createDate);
				}

				if (null != record.getModifiedDate()) {
					Date modifiedDate = FORMATTER.parse(record.getModifiedDate());
					model.setModifiedDate(modifiedDate);
				}
				if (null != record.getOpenDate()) {
					Date openDate = FORMATTER.parse(record.getOpenDate());
					if (isValidDateOrder(openDate, startDate)) {

						model.setOpenDate(openDate);
					} else {
						throw new AdminGenericException("Open Date can't be after Blackout date");
					}
				} else {
					throw new AdminGenericException("Missing Open date in request");
				}
				FscmBlackout fscmBlackout = blackoutRecords.get(0);
				for (FscmBlackout blackout : getAllBlackOuts) {
					if (fscmBlackout.getId() != null && fscmBlackout.equals(blackout)) {
						if (!fscmBlackout.getId().equals(blackout.getId())
								&& (fscmBlackout.getStartDate().equals(blackout.getStartDate())
										&& fscmBlackout.getEndDate().equals(blackout.getEndDate()))) {
							throw new AdminGenericException(
									"Update Duplicate Blackout dates for same regions are not allowed!!");
						}
					}
					if (fscmBlackout.getId() == null) {
						if (fscmBlackout.equals(blackout)
								|| (fscmBlackout.getStartDate().equals(blackout.getStartDate())
										&& fscmBlackout.getRegion().equals(blackout.getRegion()))
								|| (fscmBlackout.getStartDate().equals(blackout.getStartDate())
										&& fscmBlackout.getEndDate().equals(blackout.getEndDate())
										&& (fscmBlackout.getRegion().equals(blackout.getRegion())))) {

							throw new AdminGenericException(
									"Duplicate Blackout dates for same regions are not allowed!!");
						}
					}
				}
				blackoutModels.add(model);
			} catch (Exception e) {
				throw new AdminGenericException(e.getMessage());
			}
		}
		return dao.setBlackoutDates(blackoutModels, transactionId);

	}

	private boolean isValidDateOrder(Date dateBefore, Date dateAfter) {
		int comparison = dateBefore.compareTo(dateAfter);
		if (comparison <= 0) {
			// If dateBefore comes prior to dateAfter
			return true;
		}
		return false;
	}

	@Override
	public void removeBlackoutDates(List<FscmBlackout> inputDates, String transactionId)
			throws AdminGenericException {
		List<FscmBlackoutModel> models = new ArrayList<FscmBlackoutModel>();
		try {
			for (FscmBlackout input : inputDates) {
				FscmBlackoutModel model = new FscmBlackoutModel();
				if (null != input.getBlackoutName()) {
					model.setBlackoutName(input.getBlackoutName());
				}
				if (null != input.getCreateDate()) {
					Date createDate = FORMATTER.parse(input.getCreateDate());
					model.setCreateDate(createDate);
				}
				model.setCreatedBy(input.getCreatedBy());
				model.setId(input.getId());
				model.setModifiedBy(input.getModifiedBy());
				if (null != input.getModifiedDate()) {
					Date modifiedDate = FORMATTER.parse(input.getModifiedDate());
					model.setModifiedDate(modifiedDate);
				}
				if (null != input.getOpenDate()) {
					Date openDate = FORMATTER.parse(input.getOpenDate());
					model.setOpenDate(openDate);
				}

				if (null != input.getStartDate()) {
					Date startDate = FORMATTER.parse(input.getStartDate());
					model.setStartDate(startDate);
				}

				if (null != input.getEndDate()) {
					Date endDate = FORMATTER.parse(input.getEndDate());
					model.setEndDate(endDate);
				}
				model.setRegion(input.getRegion());
				models.add(model);
			}
			dao.removeBlackoutDates(models,transactionId);
		} catch (Exception e) {
			throw new AdminGenericException(e.getMessage());
		}

	}

	@Override
	public Boolean checkInvalidException(String exceptionRegion, String startDateStr, String endDateStr,
			String transactionId) throws AdminGenericException {
		try {
			/*
			 * Get the requested date, exception start and end date and region.
			 * Check if exception Start and end date are a super set of Blackout
			 * start/end dates OR Check if exception start/end date are a sub
			 * set of black out start/end dates. If any of the above is true,
			 * compare the open date of the said blackout date with the
			 * application date of the exception. Exception is invalid if apply
			 * date is before open date.
			 */
			Date applicationDate = new Date();
			Date exceptionStartDate = FORMATTER.parse(startDateStr);
			Date exceptionEndDate = FORMATTER.parse(endDateStr);
			List<FscmBlackoutModel> models = dao.fetchBlackoutDates(exceptionRegion, exceptionStartDate,
					exceptionEndDate,transactionId);
			for (FscmBlackoutModel model : models) {
				Integer chronology = applicationDate.compareTo(model.getOpenDate());
				if (chronology < 0) {
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			throw new AdminGenericException(
					"Error occurred while checking exception blackout status: " + e.getMessage());
		}
	}
}
