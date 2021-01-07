package com.arinno.businessmanagement.controllers;


import com.arinno.businessmanagement.model.*;
import com.arinno.businessmanagement.services.ICustomerService;
import com.arinno.businessmanagement.services.IProductService;
import com.arinno.businessmanagement.services.IProviderService;
import com.arinno.businessmanagement.services.IUserModelService;
import com.arinno.businessmanagement.util.IUtil;
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
    private IProviderService providerService;

    @Autowired
    private IUtil util;

    @PostMapping("/upload/customer")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file, Authentication authentication){

        Company company = util.getCompany(authentication);
        Map<String, Object> response = new HashMap<>();

        try {
            customerService.saveAll(excelToCustomers(file.getInputStream(), company));
        } catch (IOException e) {
            e.printStackTrace();
        }

        response.put("title", "Upload ok");
        response.put("message", "Fichero subido correctamente");
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PostMapping("/upload/product")
    public ResponseEntity<?> uploadProduct(@RequestParam("file") MultipartFile file, Authentication authentication){

        Company company = util.getCompany(authentication);
        Provider provider = providerService.findByIdAndCompany((long) 1, company);
        Map<String, Object> response = new HashMap<>();

        try {
            productService.saveAll(excelToProducts(file.getInputStream(), company, provider));
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
                Address address = new Address();
                customer.setAddress(address);


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
//                            customer.setFullAddress(currentCell.getStringCellValue());
                            break;

                        case 4:
                            customer.getAddress().setStateProvince(currentCell.getStringCellValue());
                            break;

                        case 5:
                            customer.getAddress().setTown(currentCell.getStringCellValue());
                            break;

                        case 6:
                            customer.getAddress().setPostalCode(currentCell.getStringCellValue());
                            break;

                        case 7:
                            customer.getAddress().setTelephone(currentCell.getStringCellValue());
                            break;

                        case 8:
                            customer.getAddress().setEmail(currentCell.getStringCellValue());
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

    public List<Product> excelToProducts(InputStream is, Company company, Provider provider) {
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
                product.setIvaType(IvaType.REDUCED());
                product.setProvider(provider);

                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        case 0:
                            Provider provider2 = providerService.findByCodeAndCompany(currentCell.getStringCellValue(), company);
                            product.setProvider(provider2);
                            break;

                        case 1:
                            product.setCode(currentCell.getStringCellValue());
                            break;

                        case 2:
                            product.setDescription(currentCell.getStringCellValue());
                            break;

                        case 3:
                            product.setBuyPrice(currentCell.getNumericCellValue());
                            break;

                        case 4:
                            product.setSalePrice(currentCell.getNumericCellValue());
                            break;

                        case 5:
                            if(currentCell.getNumericCellValue()==0){
                                product.setIvaType(IvaType.GENERAL);
                            }else{
                                if(currentCell.getNumericCellValue()==1){
                                    product.setIvaType(IvaType.REDUCED);
                                }else{
                                    if(currentCell.getNumericCellValue()==2){
                                        product.setIvaType(IvaType.SUPER_REDUCED);
                                    }
                                }
                            }
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
