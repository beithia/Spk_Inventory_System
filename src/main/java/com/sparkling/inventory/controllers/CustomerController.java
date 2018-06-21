package com.sparkling.inventory.controllers;

import com.sparkling.inventory.models.Customer;
import com.sparkling.inventory.repositories.CustomerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("inventory/customers/")
public class CustomerController {

    @Autowired
    CustomerDao customerDao;


    @RequestMapping("index")
    public String customer(Model model) {
        model.addAttribute("title", "Customer List");
        model.addAttribute("customers", customerDao.customerList());
        return "customers/index";
    }

    @RequestMapping("add-customer")
    public String addCustomer(Model model) {
        model.addAttribute("title", "Add Customer");
        model.addAttribute(new Customer());
        return "customers/add-customer";
    }

    @RequestMapping(value="add-customer", method= RequestMethod.POST)
    public String processAddCustomer(Model model, @ModelAttribute @Valid Customer customer, Errors error) {
        if(error.hasErrors()) {
            model.addAttribute("title", "Add Customer");
            return "customers/add-customer";
        }
        customerDao.save(customer);
        return "redirect:index";
    }



    @RequestMapping(value="edit-customer/{id}", method = RequestMethod.GET)
    public String editProduct(@PathVariable int id, Model model) {
        model.addAttribute("title", "Edit Customer");
        model.addAttribute("customer", customerDao.findById(id));
        return "customers/edit-customer";
    }



    @RequestMapping(value="edit-customer", method = RequestMethod.POST)
    public String processEditProduct(@ModelAttribute @Valid Customer customer,
                                     Errors error,
                                     Model model) {
        if(error.hasErrors()) {
            System.out.println("Entered Error Clause");
            model.addAttribute("title", "Edit");
            return "customers/edit-customer";
        }
        model.addAttribute("title", "Customer List");
        model.addAttribute("customers", customerDao.findAllByOrderByNameAsc());
        customerDao.save(customer);
        return "customers/index";
    }

    @RequestMapping(value="remove/{id}")
    public String processRemoveCustomer(@PathVariable int id, Model model) {
        customerDao.deleteById(id);
        model.addAttribute("title", "Customer List");
        model.addAttribute("customers", customerDao.findAllByOrderByNameAsc());
        return "redirect:../index";
    }

}
