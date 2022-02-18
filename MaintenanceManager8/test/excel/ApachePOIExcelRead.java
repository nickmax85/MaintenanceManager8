package excel;

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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.maintenance.db.dto.MESAnlage;
import com.maintenance.db.service.Service;
import com.maintenance.util.ApplicationProperties;

public class ApachePOIExcelRead {

	private static final String FILE_NAME = "d:/April_2017.xlsx";
	// private static final String FILE_NAME = "d:/test.xlsx";

	private static String ip;

	private ResourceBundle resources = ResourceBundle.getBundle("language");

	private List<MESAnlage> sapLines;

	public static void main(String[] args) {

		ip = null;
		if (args.length == 1) {
			ip = args[0];
		}

		new ApachePOIExcelRead();
	}

	public ApachePOIExcelRead() {

		ApplicationProperties.configure("application.properties",
				System.getProperty("user.home") + File.separator + resources.getString("appname"),
				"application.properties");
		ApplicationProperties.getInstance().setup();

		if (ip != null) {
			ApplicationProperties.getInstance().edit("db_host", ip);
		}

		real();

		for (MESAnlage mesAnlage : getData(sapLines)) {

			boolean found = false;

			if (mesAnlage.getName().contains("VG150"))
				System.out.println(mesAnlage.getAnlage());

			for (MESAnlage mesAnlageDB : Service.getInstance().getMESAnlagen()) {

				if (mesAnlage.getId() == mesAnlageDB.getId()) {
					mesAnlage.setAnlageId(mesAnlageDB.getAnlageId());
					found = true;
					break;

				} else {
					found = false;

				}
			}

			if (found)
				Service.getInstance().updateMESAnlage(mesAnlage);
			else
				Service.getInstance().insertMESAnlage(mesAnlage);

		}

	}

	private List<MESAnlage> getData(List<MESAnlage> lines) {

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

	private void real() {

		int cellIndex = 0;
		int rowIndex = 0;
		int aktStueckCellIndex = 0;
		int idCellIndex = 0;
		int linieCellIndex = 0;
		int oaCellIndex = 0;

		MESAnlage line = null;
		sapLines = new ArrayList<>();

		try {

			FileInputStream excelFile = new FileInputStream(new File(FILE_NAME));

			Workbook workbook = new XSSFWorkbook(excelFile);
			Sheet datatypeSheet = workbook.getSheetAt(1);
			Iterator<Row> iterator = datatypeSheet.iterator();

			while (iterator.hasNext()) {

				cellIndex = 0;

				Row currentRow = iterator.next();
				Iterator<Cell> cellIterator = currentRow.iterator();

				if (rowIndex > 0)
					line = new MESAnlage();

				while (cellIterator.hasNext()) {

					Cell currentCell = cellIterator.next();

					if (rowIndex == 0) {
						if (currentCell.getCellTypeEnum() == CellType.STRING) {
							if (currentCell.getStringCellValue().equalsIgnoreCase("ID"))
								idCellIndex = cellIndex;

							if (currentCell.getStringCellValue().equalsIgnoreCase("Linie"))
								linieCellIndex = cellIndex;

							if (currentCell.getStringCellValue().equalsIgnoreCase("MontStk"))
								aktStueckCellIndex = cellIndex;

							if (currentCell.getStringCellValue().equalsIgnoreCase("OA"))
								oaCellIndex = cellIndex;
						}
					}

					if (rowIndex != 0) {

						if (currentCell.getCellTypeEnum() == CellType.STRING) {

							if (cellIndex == idCellIndex) {
								// System.out.print("ID: " +
								// currentCell.getStringCellValue() + ";\t");
								line.setId(Integer.parseInt(currentCell.getStringCellValue()));
							}

							if (cellIndex == linieCellIndex) {
								// System.out.print("Linie: " +
								// currentCell.getStringCellValue() + ";\t");
								line.setName(currentCell.getStringCellValue());
							}
						}

						else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
							if (cellIndex == aktStueckCellIndex) {
								// System.out.print("MontStk: " +
								// currentCell.getNumericCellValue() + ";\t");
								line.setProdStueck((int) currentCell.getNumericCellValue());

							}

							if (cellIndex == oaCellIndex) {
								// System.out.print("MontStk: " +
								// currentCell.getNumericCellValue() + ";\t");
								line.setProdStueck((int) currentCell.getNumericCellValue());

							}

						}

					}

					cellIndex++;
				}
				if (rowIndex > 0)
					sapLines.add(line);

				rowIndex++;
				// System.out.println();

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void example() {

		try {

			FileInputStream excelFile = new FileInputStream(new File(FILE_NAME));
			Workbook workbook = new XSSFWorkbook(excelFile);
			Sheet datatypeSheet = workbook.getSheetAt(0);
			Iterator<Row> iterator = datatypeSheet.iterator();

			while (iterator.hasNext()) {

				Row currentRow = iterator.next();
				Iterator<Cell> cellIterator = currentRow.iterator();

				while (cellIterator.hasNext()) {

					Cell currentCell = cellIterator.next();
					// getCellTypeEnum shown as deprecated for version 3.15
					// getCellTypeEnum ill be renamed to getCellType starting
					// from version 4.0
					if (currentCell.getCellTypeEnum() == CellType.STRING) {
						System.out.print(currentCell.getStringCellValue() + "--");
					} else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
						System.out.print(currentCell.getNumericCellValue() + "--");
					}

				}
				System.out.println();

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}