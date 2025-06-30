package com.example.multi.app.controller.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.example.multi.app.constant.Result;
import com.example.multi.module.category.entity.Category;
import com.example.multi.module.category.service.CategoryService;
import com.example.multi.module.product.entity.Product;
import com.example.multi.module.product.entity.ProductVo;
import com.example.multi.module.product.service.ProductService;
import com.example.multi.module.productTag.entity.ProductTag;
import com.example.multi.module.productTag.service.ProductTagService;
import com.example.multi.module.tag.entity.Tag;
import com.example.multi.module.tag.service.TagService;
import com.example.multi.module.textMessage.entity.TextMessage;
import com.example.multi.module.textMessage.service.TextMessageService;
import com.example.multi.module.textMessageTask.entity.TextMessageTask;
import com.example.multi.module.textMessageTask.service.TextMessageTaskService;
import com.example.multi.module.user.entity.User;
import com.example.multi.module.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@RestController
public class ExcelController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private TagService tagService;

    @Autowired
    private ProductTagService productTagService;

    @Autowired
    private TextMessageService textMessageService;

    @Autowired
    private TextMessageTaskService textMessageTaskService;

@RequestMapping("/excel/import")
public Result<List<ProductVo>> importExcel(@RequestParam("file") MultipartFile file) throws IOException {
    List<ProductVo> dataList = new ArrayList<>();
    EasyExcel.read(file.getInputStream(), ProductVo.class, new ReadListener<ProductVo>() {
        @Override
        public void invoke(ProductVo data, AnalysisContext context) {
            dataList.add(data);
            log.info("Read data: {}", data);
            log.info("Data details: title={}, name={}, price={}", data.getTitle(), data.getName(), data.getPrice());
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            log.info("All data has been parsed");
        }

        @Override
        public void onException(Exception exception, AnalysisContext context) throws Exception {
            log.error("Error occurred while reading Excel file", exception);
            throw exception;
        }
    }).sheet().doRead();
    return Result.success(dataList);
}

    @GetMapping("/excel/exportCategory")
    public void exportCategoryExcel(HttpServletResponse response) throws IOException {
        // 获取所有分类数据
        List<Category> categoryList = categoryService.getCategorysToExcel();
        //response.setContentType("application/vnd.ms-excel");
        //用于表示旧版本的 Excel 文件（如 .xls 格式）。
        //优点：大多数旧版本的 Excel 软件（如 Excel 2003、Excel 2007 等）都能打开这种格式的文件。
        //缺点：这种格式的文件大小有限制，通常最大为 2MB 左右，且不支持一些较新的 Excel 功能，如复杂的公式、高级图表等。
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        //用于表示较新的 Excel 文件（如 .xlsx 格式）。
        //优点：支持更大的文件大小（通常最大为 50MB 左右），并且支持更复杂的 Excel 功能，如高级公式、高级图表、条件格式等。
        //缺点：较旧版本的 Excel 软件（如 Excel 2003）可能无法打开这种格式的文件，但可以通过安装兼容包来解决。
        response.setCharacterEncoding("utf-8");
        String fileName = "测试文件.xlsx";
        fileName = java.net.URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        try {
            //EasyExcel.write(response.getOutputStream(), ProductVo.class).sheet("分类表").doWrite(categoryList);
            // 导出Excel
            EasyExcel.write(response.getOutputStream(), Category.class)
                    .sheet("分类表")
                    .doWrite(categoryList);
        } catch (Exception e) {
            log.error("Error occurred while exporting Excel file", e);
            throw e;
        }
    }


    @GetMapping("/excel/exportAllTables")
    public void exportAllTablesToZip(HttpServletResponse response) throws IOException {
        response.setContentType("application/zip");
        response.setCharacterEncoding("utf-8");
        String fileName = "所有表数据.zip";
        fileName = java.net.URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);

        try {
            // 1. 导出分类表
            exportTableToZip(zos, "分类表.xlsx", "分类表", Category.class, categoryService.getCategorysToExcel());

            // 2. 导出产品表
            List<Product> productList = productService.getProductsToExcel();
            exportTableToZip(zos, "产品表.xlsx", "产品表", Product.class, productList);

            // 3. 导出用户表
            List<User> userList = userService.getUsersToExcel();
            exportTableToZip(zos, "用户表.xlsx", "用户表", User.class, userList);

            // 4. 导出标签表
            List<Tag> tagList = tagService.getTagsToExcel();
            exportTableToZip(zos, "标签表.xlsx", "标签表", Tag.class, tagList);

            // 5. 导出产品标签关联表
            List<ProductTag> productTagList = productTagService.getProductTagsToExcel();
            exportTableToZip(zos, "产品标签关联表.xlsx", "产品标签关联表", ProductTag.class, productTagList);

            // 6. 导出短信表
            List<TextMessage> textMessageList = textMessageService.getTextMessagesToExcel();
            exportTableToZip(zos, "短信表.xlsx", "短信表", TextMessage.class, textMessageList);

            // 7. 导出短信任务表
            List<TextMessageTask> textMessageTaskList = textMessageTaskService.getTextMessageTasksToExcel();
            exportTableToZip(zos, "短信任务表.xlsx", "短信任务表", TextMessageTask.class, textMessageTaskList);

            log.info("所有表数据导出成功");
        } catch (Exception e) {
            log.error("导出所有表数据时发生错误", e);
            throw e;
        } finally {
            zos.close();
        }

        response.getOutputStream().write(baos.toByteArray());
    }
    private <T> void exportTableToZip(ZipOutputStream zos, String fileName, String sheetName, Class<T> clazz, List<T> dataList) throws IOException {
        ByteArrayOutputStream sheetBaos = new ByteArrayOutputStream();
        EasyExcel.write(sheetBaos, clazz).sheet(sheetName).doWrite(dataList);
        zos.putNextEntry(new ZipEntry(fileName));
        zos.write(sheetBaos.toByteArray());
        zos.closeEntry();
        log.info("表 {} 导出成功，共 {} 条记录", sheetName, dataList.size());
    }
}