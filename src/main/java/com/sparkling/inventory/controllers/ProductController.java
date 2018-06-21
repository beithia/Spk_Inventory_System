package com.sparkling.inventory.controllers;

import com.sparkling.inventory.models.Product;
import com.sparkling.inventory.models.Transaction;
import com.sparkling.inventory.repositories.ProductDao;
import com.sparkling.inventory.repositories.TransactionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.validation.Valid;

@Controller
@RequestMapping("inventory/products/")
public class ProductController {

    @Autowired
    ProductDao productDao;

    @Autowired
    TransactionDao transactionDao;

    @RequestMapping("index")
    public String products(Model model) {
        model.addAttribute("title", "Product List");
        model.addAttribute("products", productDao.getSpkStock());
        return "products/index";
    }

    @RequestMapping("add-product")
    public String addProduct(Model model) {
        model.addAttribute("title", "Add Product");
        model.addAttribute(new Product());
        return "products/add-product";
    }

    @RequestMapping(value="add-product", method= RequestMethod.POST)
    public String processAddProduct(Model model, @ModelAttribute @Valid Product product, Errors error) {
        if(error.hasErrors()) {
            model.addAttribute("title", "Add Product");
            return "products/add-product";
        }
        productDao.save(product);
        return "redirect:index";
    }

    @RequestMapping(value="edit-product/{id}", method = RequestMethod.GET)
    public String editProduct(@PathVariable int id, Model model) {
        model.addAttribute("title", "Edit Product");
        model.addAttribute("product", productDao.findById(id));
        return "products/edit-product";
    }

    @RequestMapping(value="edit-product", method = RequestMethod.POST)
    public String processEditProduct(@ModelAttribute("product") Product product,
                                     Errors error,
                                     Model model) {
        if(error.hasErrors()) {
            model.addAttribute("title", "Edit");
            return "redirect:index";
        }
        model.addAttribute("title", "Product List");
        model.addAttribute("products", productDao.findAllByOrderByNameAsc());
        productDao.save(product);
        return "products/index";
    }

    @RequestMapping(value="remove/{id}")
    public String processRemoveProduct(@PathVariable int id, Model model) {
        productDao.deleteById(id);
        model.addAttribute("title", "Product List");
        model.addAttribute("products", productDao.findAllByOrderByNameAsc());
        return "redirect:../index";
    }


   @RequestMapping(value="add-stock", method = RequestMethod.POST)
    public String addStock(@ModelAttribute("prodId") int prodId,
                           @ModelAttribute("quantity") int qty,
                           Model model){
       Transaction tx = new Transaction();
       tx.setQuantity(qty);
       tx.setCustomer_id(0);
       tx.setProduct_id(prodId);
       transactionDao.save(tx);
       System.out.println("Product Id: " + prodId);
        return "redirect:index";
    }
}


