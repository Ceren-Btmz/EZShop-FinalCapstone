package org.yearup.data;

import org.springframework.web.bind.annotation.RequestMapping;
import org.yearup.models.ShoppingCart;

import java.util.List;

public interface ShoppingCartDao {

    @RequestMapping()
    ShoppingCart getByUserId(int userId);
    // add additional method signatures here

}
