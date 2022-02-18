package com.maintenance.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.maintenance.db.dto.MESAnlage;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;

public class MESAnlageExcelRead {

	private static final Logger logger = Logger.getLogger(MESAnlageExcelRead.class);
	private ResourceBundle resources = ResourceBundle.getBundle("language");

	private int sheetNr;
	private String filename;
	private List<MESAnlage> anlagen;

	private static String ip;

	public static void main(String[] args) {

		ip = null;
		if (args.length == 1) {
			ip = args[0];
		}

		ApplicationProperties.configure("application.properties",
				System.getProperty("user.home") + File.separator + "MaintenanceManager", "application.properties");
		ApplicationProperties.getInstance().setup();

		if (ip != null) {
			ApplicationProperties.getInstance().edit("db_host", ip);
		}

		new MESAnlageExcelRead("d:/April_2017.xlsx", 1);
	}

	public MESAnlageExcelRead(String filename, int sheetNr) {

		this.filename = filename;
		this.sheetNr = sheetNr;

	}

	public List<MESAnlage> calcProdStueck(List<MESAnlage> lines) {

		Map<Integer, MESAnlage> lineMap = new HashMap<>();
		MESAnlage selectedLine;

		for (MESAnlage line : lines) {

			selectedLine = lineMap.get(line.getId());

			if (selectedLine == null)
				lineMap.put(line.getId(), line);
			else {
				selectedLine.setProdStueck(selectedLine.getProdStueck() + line.getProdStueck());
				lineMap.put(line.getId(), selectedLine);

			}

		}

		List<MESAnlage> list = new ArrayList<MESAnlage>(lineMap.values());

		return list;

	}

	public List<MESAnlage> readExcel(TextArea importResultTextArea) throws IllegalArgumentException {

		int cellIndex = 0;
		int rowIndex = 0;
		int aktStueckCellIndex = 0;
		int idCellIndex = 0;
		int linieCellIndex = 0;

		boolean idCellFound = false;
		boolean linieCellIndexFound = false;
		boolean aktStueckCellIndexFound = false;

		MESAnlage anlage = null;
		List<MESAnlage> anlagen = new ArrayList<>();

		try {

			FileInputStream excelFile = new FileInputStream(new File(filename));
			Workbook workbook = new XSSFWorkbook(excelFile);
			// logger.info(workbook.getAllNames());

			Sheet datatypeSheet = workbook.getSheetAt(sheetNr);
			Iterator<Row> iterator = datatypeSheet.iterator();

			while (iterator.hasNext()) {

				cellIndex = 0;

				Row currentRow = iterator.next();

				// logger.info(currentRow.getRowNum());
				Iterator<Cell> cellIterator = currentRow.iterator();

				if (rowIndex > 0)
					anlage = new MESAnlage();

				while (cellIterator.hasNext()) {

					Cell currentCell = cellIterator.next();

					if (rowIndex == 0) {
						if (currentCell.getCellTypeEnum() == CellType.STRING) {
							if (currentCell.getStringCellValue().equalsIgnoreCase("ID")) {
								// logger.info("Zelle: " +
								// currentCell.getStringCellValue());
								idCellIndex = cellIndex;
								idCellFound = true;
							}

							if (currentCell.getStringCellValue().equalsIgnoreCase("Linie")) {
								// logger.info("Zelle: " +
								// currentCell.getStringCellValue());
								linieCellIndex = cellIndex;
								linieCellIndexFound = true;

							}

							if (currentCell.getStringCellValue().equalsIgnoreCase("MontStk")) {
								// logger.info("Zelle: " +
								// currentCell.getStringCellValue());
								aktStueckCellIndex = cellIndex;
								aktStueckCellIndexFound = true;
							}
						}

					}

					if (rowIndex != 0 && idCellFound && linieCellIndexFound && aktStueckCellIndexFound) {

						if (currentCell.getCellTypeEnum() == CellType.STRING) {

							if (cellIndex == idCellIndex) {

								// logger.info("ID: " +
								// currentCell.getStringCellValue());
								anlage.setId(Integer.parseInt(currentCell.getStringCellValue()));
							}

							if (cellIndex == linieCellIndex) {

								// logger.info("Linie: " +
								// currentCell.getStringCellValue());
								anlage.setName(currentCell.getStringCellValue());
							}
						}

						else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
							if (cellIndex == aktStueckCellIndex) {

								// logger.info("MontStk: " +
								// currentCell.getNumericCellValue());
								anlage.setProdStueck((int) currentCell.getNumericCellValue());

							}

						}

					}

					cellIndex++;
				}

				if (rowIndex == 0)
					if (!idCellFound || !linieCellIndexFound || !aktStueckCellIndexFound) {
						if (!idCellFound)
							showExcelAlertDialog("ID");
						if (!linieCellIndexFound)
							showExcelAlertDialog("Linie");
						if (!aktStueckCellIndexFound)
							showExcelAlertDialog("MontStk");

						break;
					}

				if (rowIndex > 0)
					anlagen.add(anlage);

				rowIndex++;
				// System.out.println();

			}

			excelFile.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return calcProdStueck(anlagen);
	}

	private void showExcelAlertDialog(String text) {

		Alert alert = new Alert(AlertType.ERROR);

		alert.setTitle("Excel Import");
		alert.setHeaderText("Excel Import");
		alert.setContentText("Spaltenname: " + text + " wurde in Excel Datei nicht gefunden");

		alert.showAndWait();

	}

}
