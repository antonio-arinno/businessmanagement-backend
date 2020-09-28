package com.arinno.businessmanagement.controllers;


import com.arinno.businessmanagement.model.Company;
import com.arinno.businessmanagement.model.Customer;
import com.arinno.businessmanagement.model.Product;
import com.arinno.businessmanagement.services.ICustomerService;
import com.arinno.businessmanagement.services.IProductService;
import com.arinno.businessmanagement.services.IUserModelService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



import org.apache.poi.ss.usermodel.Row;




import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@CrossOrigin(origins = {"https://businessmanagement-7b334.web.app", "http://localhost:4200", "*"})
@RestController
@RequestMapping("/api")
public class ExternalDataRestController {


    @Autowired
    private ICustomerService customerService;

    @Autowired
    private IProductService productService;

    @Autowired
    private IUserModelService userModelService;


    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file, Authentication authentication){

        Company company = userModelService.findByUsername(authentication.getName()).getCompany();

        Map<String, Object> response = new HashMap<>();

        try {
            customerService.saveAll(excelToCustomers(file.getInputStream(), company));
        } catch (IOException e) {
            e.printStackTrace();
        }

        response.put("title", "Upload ok");
        response.put("message", "Fichero subido correctamente");
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
/*
        response.put("error", "Error al realizar el insert en la base de datos");
        response.put("message", "error");
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
*/
    }

    @PostMapping("/upload/product")
    public ResponseEntity<?> uploadProduct(@RequestParam("file") MultipartFile file, Authentication authentication){

        Company company = userModelService.findByUsername(authentication.getName()).getCompany();
        Map<String, Object> response = new HashMap<>();

        try {
            productService.saveAll(excelToProducts(file.getInputStream(), company));
        } catch (IOException e) {
            e.printStackTrace();
        }

        response.put("title", "Upload ok");
        response.put("message", "Fichero subido correctamente");
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

    }

    public static List<Customer> excelToCustomers(InputStream is, Company company) {
        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheet("Hoja1");
            Iterator<Row> rows = sheet.iterator();

            List<Customer> customers = new ArrayList<Customer>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                org.apache.poi.ss.usermodel.Row currentRow = rows.next();

                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();

                Customer customer = new Customer();
                customer.setCompany(company);
                customer.setCreateAt(new Date());


                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        case 0:
                            customer.setCode(currentCell.getStringCellValue());
                            break;

                        case 1:
                            customer.setTaxId(currentCell.getStringCellValue());
                            break;

                        case 2:
                            customer.setName(currentCell.getStringCellValue());
                            break;

                        case 3:
                            customer.setAddress(currentCell.getStringCellValue());
                            break;

                        case 4:
                            customer.setStateProvince(currentCell.getStringCellValue());
                            break;

                        case 5:
                            customer.setTown(currentCell.getStringCellValue());
                            break;

                        case 6:
                            customer.setPostalCode(currentCell.getStringCellValue());
                            break;

                        case 7:
                            customer.setTelephone(currentCell.getStringCellValue());
                            break;

                        case 8:
                            customer.setEmail(currentCell.getStringCellValue());
                            break;

                        default:
                            break;
                    }

                    cellIdx++;
                }

                customers.add(customer);
            }

            workbook.close();

            return customers;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

    public static List<Product> excelToProducts(InputStream is, Company company) {
        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheet("Hoja1");
            Iterator<Row> rows = sheet.iterator();

            List<Product> products = new ArrayList<Product>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                org.apache.poi.ss.usermodel.Row currentRow = rows.next();

                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();


                Product product = new Product();
                product.setCompany(company);
                product.setCreateAt(new Date());


                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        case 0:
                            product.setCode(currentCell.getStringCellValue());
                            break;

                        case 1:
                            product.setDescription(currentCell.getStringCellValue());
                            break;

                        case 2:
                            product.setPrice(currentCell.getNumericCellValue());
                            break;

                        default:
                            break;
                    }

                    cellIdx++;
                }

                products.add(product);
            }

            workbook.close();

            return products;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }


}
