package com.example.demo.Controllers;

import java.util.List;

import com.example.demo.Entities.ProductsEntity;
import com.example.demo.Models.ProductsModel;
import com.example.demo.Repositories.ProductsRepository;
import com.example.demo.services.EToM;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( path = "/products" )
public class ProductsController {

    @Autowired
    ProductsRepository productsRepository ;
    @Autowired
    EToM service ;  

    @PostMapping( path = "/test")
    public ProductsEntity koko(){
        return new ProductsEntity();
    }

    @PostMapping( path = "/create")
    public ProductsModel createProducts( @RequestBody ProductsModel productsModel ){
        ProductsEntity productsEntity = new ProductsEntity();
        if ( productsRepository.findByName(productsModel.getName()).isPresent()){
            productsModel.setName("error");
            return productsModel ;
        }
        else{
            productsEntity = service.convertProductFromModelToEntity(productsModel);
            productsEntity = productsRepository.save(productsEntity);
            productsModel  = service.convertProductFromEntityToModel(productsEntity);
            return productsModel ;
        }
    }

    @GetMapping( path = "/fetch")
    public List<ProductsModel> fetchProducts(){
        return service.convertProductFromListEntityToListModel(productsRepository.fetchAllProducts().get());
    }

    @PostMapping( path = "/update")
    public boolean updateProduct(@RequestBody ProductsModel productsModel ){

        if (productsRepository.findByName(productsModel.getName()).isPresent()){

            productsRepository.updateProduct(productsModel.getName(), productsModel.getDescription(), productsModel.getCategory(), productsModel.getQuantities(), productsModel.getPrice(), productsModel.getId());
            return true ;
        }
        else
            return false ;
        

        /*
        ProductsEntity productsEntity = new ProductsEntity();
        if (productsRepository.findById(productsModel.getId()).isPresent()){

            productsEntity = productsRepository.findById(productsModel.getId()).get();
            productsRepository.delete(productsEntity);
            productsEntity = service.convertProductFromModelToEntity(productsModel);
            productsEntity = productsRepository.save(productsEntity);
            return service.convertProductFromEntityToModel(productsEntity);

        }
        else{
            productsEntity.setName("error");
            return service.convertProductFromEntityToModel(productsEntity);
        }
        */
    }

    

    
}