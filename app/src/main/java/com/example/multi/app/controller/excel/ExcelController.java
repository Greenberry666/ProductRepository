package com.example.multi.app.controller.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.example.multi.module.product.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.util.List;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@RestController
public class ExcelController {

    @RequestMapping("/excel/import")
    public List<Object> importExcel(@RequestParam("file") MultipartFile file) throws IOException {
        List<Object> dataList = new ArrayList<>();
        EasyExcel.read(file.getInputStream(), Product.class, new ReadListener<Product>() {
            @Override
            public void invoke(Product data, AnalysisContext context) {
                dataList.add(data);
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                // Do nothing
            }
        }).sheet().doRead();
        return dataList;
    }

    @RequestMapping("/excel/export")
    public void exportExcel(HttpServletResponse response) throws IOException {
        List<Product> data = getData(); // 获取要导出的数据
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=exported_data.xlsx");
        try {
            EasyExcel.write(response.getOutputStream(), Product.class).sheet("Sheet1").doWrite(data);
        } catch (Exception e) {
            log.error("Error occurred while exporting Excel file", e);
            throw e;
        }
    }

    private List<Product> getData() {
        List<Product> productList = new ArrayList<>();
        productList.add(new Product().setTitle("产品1").setName("名称1").setImages("image1.jpg").setInfo("信息1").setPrice(100).setDetailedTitle("详细标题1").setDetailed("详细信息1").setWeight(10).setCreateTime(1633072800).setUpdateTime(1633072800).setIsDeleted(0).setCategoryId(BigInteger.valueOf(1)));
        productList.add(new Product().setTitle("产品2").setName("名称2").setImages("image2.jpg").setInfo("信息2").setPrice(200).setDetailedTitle("详细标题2").setDetailed("详细信息2").setWeight(20).setCreateTime(1633072800).setUpdateTime(1633072800).setIsDeleted(0).setCategoryId(BigInteger.valueOf(2)));
        return productList;
    }

    @RequestMapping("/excel/exportZip")
    public void exportExcelZip(HttpServletResponse response) throws IOException {
        List<List<Product>> dataList = getDataList(); // 获取要导出的数据列表
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=exported_data.zip");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);
        try {
            for (int i = 0; i < dataList.size(); i++) {
                List<Product> data = dataList.get(i);
                String fileName = "Sheet" + (i + 1) + ".xlsx";
                ByteArrayOutputStream sheetBaos = new ByteArrayOutputStream();
                EasyExcel.write(sheetBaos, Product.class).sheet("Sheet" + (i + 1)).doWrite(data);
                zos.putNextEntry(new ZipEntry(fileName));
                zos.write(sheetBaos.toByteArray());
                zos.closeEntry();
            }
        } catch (Exception e) {
            log.error("Error occurred while exporting Excel files to zip", e);
            throw e;
        } finally {
            zos.close();
        }
        response.getOutputStream().write(baos.toByteArray());
    }

    private List<List<Product>> getDataList() {
        // 这里根据实际情况获取数据
        return new ArrayList<>();
    }
}