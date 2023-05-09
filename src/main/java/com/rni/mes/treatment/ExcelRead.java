package com.rni.mes.treatment;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.rni.mes.records.LieuMesure;

public class ExcelRead {

	//check that file is of excel type or not
    @SuppressWarnings("null")
	public static boolean checkExcelFormat(MultipartFile file) {

        String contentType = file.getContentType();

		
        if ( contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            return true;
        } else {
            return false;
        }

    }
    
    public static List<LieuMesure> convertExcelToMap(InputStream is){
    	List<LieuMesure> liste = new ArrayList<>();
    	try {
    		XSSFWorkbook workbook = new XSSFWorkbook(is);

            XSSFSheet sheet = workbook.getSheet("Liste des Lieux à Mesurer");
            int rowNumber = 0;
            Iterator<Row> iterator = sheet.iterator();
            while(iterator.hasNext()) {
            	Row row = iterator.next();
            	
            	if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
            	
            	Iterator<Cell> cells = row.iterator();

                int cid = 0;
                
                //attributs 
                String region = null; String province = null; String ville = null; String nomSite = null; //String adresse;
                Double longitude = null; Double latitude = null; String prioritaire = null; Date dateMesure = null;
                String largeBande = null; Float moyenneSpatiale = null; String bandeEtroite = null; String commentaire = null;
                
                while(cells.hasNext()) {
                	Cell cell = cells.next();
                	
                	if(cell==null) {
                		continue;
                	}else {
                		switch (cid) {
							case 0:
	//							lieu.setNumeroLieu((int) cell.getNumericCellValue());
								System.out.println(cell.getNumericCellValue());
								break;
							case 1:
								region = cell.getStringCellValue();
								break;
							case 2:
								province = cell.getStringCellValue();
								break;
							case 3:
								ville = cell.getStringCellValue();
								break;
							case 4:
								nomSite = cell.getStringCellValue();
								break;
							case 5:
//								adresse = cell.getStringCellValue();
								System.out.println(cell.getStringCellValue());
								break;
							case 6:
								String lgn = cell.getStringCellValue();
								Integer nbLg = lgn.length();
								//processus de suppresion des derniers chaines de caracteres
								String suppChaine = lgn.substring(0, (nbLg-3));
								longitude = - Double.valueOf(suppChaine);
								break;
							case 7:
								String lat = cell.getStringCellValue();
								Integer nbLa = lat.length();
								//processus de suppression des derniers chaines de caracteres
								String suppChaine2 = lat.substring(0, (nbLa-3));
								latitude = Double.valueOf(suppChaine2);
								
								break;
							case 8:
								prioritaire = cell.getStringCellValue();
								break;
							case 9:
								dateMesure = cell.getDateCellValue();
								break;
							case 10:
								largeBande = cell.getStringCellValue();
								break;
							case 11:
								moyenneSpatiale = (float) cell.getNumericCellValue();
								break;
							case 12:
								bandeEtroite = cell.getStringCellValue();
								break;
							case 13:
								commentaire = cell.getStringCellValue();
								break;
							default:
								break;
					}
                	}
                	
                	cid++;
                }
	            @SuppressWarnings("null")
				LieuMesure lieuMesure = new LieuMesure(null, nomSite, null, region,
					            		   province, ville, null, longitude, latitude,
					            		   prioritaire, dateMesure, moyenneSpatiale, largeBande, bandeEtroite,
					            		   commentaire, null);
	            liste.add(lieuMesure);
            }
            workbook.close();
		} catch (Exception e) {
			 e.printStackTrace();
		}
    	
		return liste;
    }
}
