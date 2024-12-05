package CoffeeApp.storageservice.util;

import CoffeeApp.storageservice.dto.acceptanceDto.AcceptanceFromFileDto;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class WordDocumentParser {

    private static final int[] requiredColumns = {2, 5, 6, 12};

    public List<AcceptanceFromFileDto> parseDocument(MultipartFile file) {
        List<AcceptanceFromFileDto> allGoods = new ArrayList<>();
        try (XWPFDocument doc = new XWPFDocument(file.getInputStream())) {
            List<XWPFTable> tables = doc.getTables();
            int lastTableIndex = tables.size() -1;
            for (int i = 0; i < tables.size(); i++) {
                List<String[]> arrays = new ArrayList<>();
                if (i == lastTableIndex){
                    for (int j = 3; j < tables.get(i).getNumberOfRows() - 2; j++) {
                        parseTable(tables, i, arrays, j);
                    }
                } else {
                    for (int j = 3; j < tables.get(i).getNumberOfRows(); j++) {
                        parseTable(tables, i, arrays, j);
                    }
                }

                for (String[] array : arrays) {
                    for (int j = 0; j <= array.length - 4; j += 4) {
                        String name = array[j];
                        float price = Float.parseFloat(array[j + 3].replace(",", ".").replace(" ", "")) / Float.parseFloat(array[j + 2].replace(",", "."));
                        float quantity = Float.parseFloat(array[j + 2].replace(",", "."));
                        AcceptanceFromFileDto acceptance = new AcceptanceFromFileDto(name, price, quantity);
                        allGoods.add(acceptance);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allGoods;
    }

    private void parseTable(List<XWPFTable> tables, int i, List<String[]> arrays, int j) {
        String[] array = new String[0];
        for (int colIndex : requiredColumns) {
            XWPFTableCell cell = tables.get(i).getRow(j).getCell(colIndex);
            String item = cell.getText().trim();
            array = concatenateArrays(array, item.split("; "));
        }
        arrays.add(array);
    }

    private String[] concatenateArrays(String[] arr1, String[] arr2) {
        String[] result = new String[arr1.length + arr2.length];
        System.arraycopy(arr1, 0, result, 0, arr1.length);
        System.arraycopy(arr2, 0, result, arr1.length, arr2.length);
        return result;
    }
}
