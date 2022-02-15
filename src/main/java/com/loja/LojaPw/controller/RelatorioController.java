package com.loja.LojaPw.controller;

import com.loja.LojaPw.repository.ProdutoRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.*;
import java.sql.SQLException;

@Controller
@RequestMapping("/relatorio-jasper")
public class RelatorioController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/clientes")
    public void relatorioFuncionarios(HttpServletResponse servletResponse) throws JRException, SQLException, IOException {
        servletResponse.setContentType("application/pdf");
        servletResponse.setHeader("Content-Disposition", "attachment;filename=clientes.pdf");
        OutputStream outputStream = servletResponse.getOutputStream();
        InputStream jasperFile = this.getClass().getResourceAsStream("/clientes.jasper");
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFile);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource.getConnection());
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
    }
    @GetMapping("/produtos")
    public void relatorioProdutos(HttpServletResponse servletResponse) throws JRException, SQLException, IOException {
        servletResponse.setContentType("application/pdf");
        servletResponse.setHeader("Content-Disposition", "attachment;filename=produtos.pdf");
        OutputStream outputStream = servletResponse.getOutputStream();
        InputStream jasperFile = this.getClass().getResourceAsStream("/produtos.jasper");
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFile);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource.getConnection());
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
    }

}

