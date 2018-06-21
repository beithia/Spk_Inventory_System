package com.sparkling.inventory.controllers;

import com.sparkling.inventory.models.Customer;
import com.sparkling.inventory.models.Transaction;
import com.sparkling.inventory.repositories.CustomerDao;
import com.sparkling.inventory.repositories.ProductDao;
import com.sparkling.inventory.repositories.TransactionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("inventory")
public class InventoryController {

    @Autowired
    CustomerDao customerDao;

    @Autowired
    ProductDao productDao;

    @Autowired
    TransactionDao transactionDao;

    @RequestMapping("")
    public String index(Model model) {
        model.addAttribute("title", "Home");
        model.addAttribute("customers", customerDao.customerList());
        return "index";
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String processFetch(@ModelAttribute Customer customer,
                               @ModelAttribute("dateFrom") Date dateFrom,
                               @ModelAttribute("dateTo") Date dateTo,
                               @ModelAttribute("quantity") int qty,
                               @ModelAttribute("prodId") int prodId,
                               Model model) {


        /*
        Since by default the time in the date object is 00:00:00 the dateTo object time is set to 23:59:59
        below. This way if the products are searched for the same day, it would be searched as
        MM-dd-yyyy 00:00:00 to MM-dd-YYYY 23:59:59 instead of MM-dd-yyyy 00:00:00 to MM-dd-yyyy 00:00:00
        which would return no results.
         */
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTo);
        calendar.add(calendar.HOUR, 23);
        calendar.add(calendar.MINUTE, 59);
        calendar.add(calendar.SECOND, 59);
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        String newDateFrom = df.format(dateFrom);
        String newDateTo = df.format(dateTo);



        //Run this only if an attempt to add products has been performed.
        if(qty > 0) {
            int productStock = productDao.findSpkStockByProductId(prodId);
            //Check if there's enough product in stock to fulfill the order.
            if(productStock < qty && productStock <= 0) {
                List<Object[]> productList = productDao.findProductsByCustomerId(customer.getId(), dateFrom, calendar.getTime());
                double totalPrice = productDao.findTotalPrice(customer.getId(), dateFrom, calendar.getTime());
                model.addAttribute("title", "Home");
                model.addAttribute("customers", customerDao.findAllByOrderByNameAsc());
                model.addAttribute("customer", customerDao.findById(customer.getId()));
                model.addAttribute("spkStock", productDao.getSpkStock());
                model.addAttribute("products", productList);
                model.addAttribute("period", "From " + newDateFrom + " To " + newDateTo);
                model.addAttribute("totalPrice", totalPrice);
                model.addAttribute("stockError", 1);
                return "index";
            }
            else {
            //Add product qty to customer inventory
            Transaction tx = new Transaction();
            tx.setCustomer_id(customer.getId());
            tx.setProduct_id(prodId);
            tx.setQuantity(qty);
            transactionDao.save(tx);

            //Remove product qty from SPK inventory
            Transaction spkTx = new Transaction();
            spkTx.setCustomer_id(0);
            spkTx.setProduct_id(prodId);
            spkTx.setQuantity(qty * -1);
            transactionDao.save(spkTx);
            }
        }
        List<Object[]> productList = productDao.findProductsByCustomerId(customer.getId(), dateFrom, calendar.getTime());
        double totalPrice = productDao.findTotalPrice(customer.getId(), dateFrom, calendar.getTime());
        model.addAttribute("title", "Home");
        model.addAttribute("customers", customerDao.findAllByOrderByNameAsc());
        model.addAttribute("customer", customerDao.findById(customer.getId()));
        model.addAttribute("spkStock", productDao.getSpkStock());
        model.addAttribute("products", productList);
        model.addAttribute("period", "From " + newDateFrom + " To " + newDateTo);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("stockError", 0);
        return "index";
    }
}

