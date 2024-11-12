package com.tripwhiz.tripwhizuserback.cart.repository;

import com.tripwhiz.tripwhizuserback.cart.domain.CartDetails;
import com.tripwhiz.tripwhizuserback.cart.dto.CartListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartDetailsRepository extends JpaRepository<CartDetails, Long> {


    @Query("""
        SELECT 
           new com.tripwhiz.tripwhizuserback.cart.dto.CartListDTO(
            p.pno, p.pname, p.price , attach.filename, 
            cd.qty 
            ) 
        FROM 
            MemberEntity m 
            left join Cart c ON c.member = m 
            left join CartDetails cd ON cd.cart = c 
            join Product p ON p = cd.product
            left join p.attachFiles attach
         where m.email = :email
         and attach.ord = 0
         group by p 
    """)
    Page<CartListDTO[]> listOfMember(@Param("email") String email, Pageable pageable);


}
