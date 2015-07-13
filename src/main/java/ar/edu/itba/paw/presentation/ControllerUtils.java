package ar.edu.itba.paw.presentation;

import java.util.Collection;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.domain.enrollment.Enrollment;
import ar.edu.itba.paw.domain.enrollment.EnrollmentRepo;
import ar.edu.itba.paw.domain.enrollment.Purchase;
import ar.edu.itba.paw.domain.enrollment.PurchaseRepo;
import ar.edu.itba.paw.domain.service.Service;
import ar.edu.itba.paw.domain.service.ServiceRepo;
import ar.edu.itba.paw.domain.user.Person.PaymentMethod;

@Component
public class ControllerUtils {
	
	private final ServiceRepo serviceRepo;
	private final EnrollmentRepo enrollmentRepo;
	private final PurchaseRepo purchaseRepo;

	@Autowired
	public ControllerUtils(final ServiceRepo serviceRepo, final EnrollmentRepo enrollmentRepo, 
			final PurchaseRepo purchaseRepo) {
		this.serviceRepo = serviceRepo;
		this.enrollmentRepo = enrollmentRepo;
		this.purchaseRepo = purchaseRepo;
	}
	
	public void createNewEnrollmentsSheet(final HSSFWorkbook workbook) {
		final Service service = serviceRepo.get("ceitba");
		final Collection<Enrollment> enrollments = enrollmentRepo.getBilledNewEnrollments(service, DateTime.now(), 
				DateTime.now().minusMonths(1));
		createEnrollmentSheet(workbook, "Altas", enrollments);
	}

	public void createCancelledEnrollmentsSheet(final HSSFWorkbook workbook) {
		final Service service = serviceRepo.get("ceitba");
		final Collection<Enrollment> enrollments = enrollmentRepo.getBilledCancelledEnrollments(service, DateTime.now(), 
				DateTime.now().minusMonths(1));
		createEnrollmentSheet(workbook, "Bajas", enrollments);
	}

	public void createActiveEnrollmentsSheet(final HSSFWorkbook workbook) {
		for (final Service service : serviceRepo.getActive()) {
			final Collection<Enrollment> enrollments = enrollmentRepo.getBilledActive(service);
			createEnrollmentSheet(workbook, service.getName(), enrollments);
		}
	}
	
	public void createPurchaseSheet(final HSSFWorkbook workbook) {
		final HSSFSheet sheet = workbook.createSheet("Compras");
		//Create a new row in current sheet
		int rowNum = 0;
		final HSSFRow firstRow = sheet.createRow(rowNum);
		firstRow.createCell(0).setCellValue("Legajo");
		firstRow.createCell(1).setCellValue("Nombre");
		firstRow.createCell(2).setCellValue("Apellido");
		firstRow.createCell(3).setCellValue("Monto");
		firstRow.createCell(4).setCellValue("Detalle");

		for (final Purchase p : purchaseRepo.getPending()) {
			if (p.getPerson().getPaymentMethod() == PaymentMethod.BILL) {
				rowNum++;
				final HSSFRow row = sheet.createRow(rowNum);
				row.createCell(0).setCellValue(p.getPerson().getLegacy());
				row.createCell(1).setCellValue(p.getPerson().getFirstName());
				row.createCell(2).setCellValue(p.getPerson().getLastName());
				row.createCell(4).setCellValue(p.getProduct().getValue());
				row.createCell(3).setCellValue(p.getProduct().getName());
			}
		}
		return;
	}
	
	public void createEnrollmentSheet(final HSSFWorkbook workbook, final String name, final Collection<Enrollment> enrollments) {
		final HSSFSheet sheet = workbook.createSheet(name);
		//Create a new row in current sheet
		int rowNum = 0;
		final HSSFRow firstRow = sheet.createRow(rowNum);
		firstRow.createCell(0).setCellValue("Legajo");
		firstRow.createCell(1).setCellValue("Nombre");
		firstRow.createCell(2).setCellValue("Apellido");
		firstRow.createCell(3).setCellValue("Inicio");
		firstRow.createCell(4).setCellValue("Fin");

		for (final Enrollment e : enrollments) {
			rowNum++;
			final HSSFRow row = sheet.createRow(rowNum);
			row.createCell(0).setCellValue(e.getPerson().getLegacy());
			row.createCell(1).setCellValue(e.getPerson().getFirstName());
			row.createCell(2).setCellValue(e.getPerson().getLastName());
			row.createCell(3).setCellValue(e.getFormatedStartDate());
			row.createCell(4).setCellValue(e.getFormatedEndDate());
		}
		return;
	}


}
