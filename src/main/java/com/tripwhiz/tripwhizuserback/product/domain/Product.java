package com.tripwhiz.tripwhizuserback.product.domain;

//import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pno;

    @Column(nullable = false, length = 50)
    private String pname;

    @Lob
    private String pdesc;

    private int price;

//    @ElementCollection
//    @Builder.Default
//    private Set<AttachFile> attachFiles = new HashSet<>();

    private boolean delFlag;

    public void changeDelFlag(boolean newDelFlag) {
        this.delFlag = newDelFlag;
    }

//    public void addFile(String filename){
//        attachFiles.add(new AttachFile(attachFiles.size(), filename));
//    }
//
//    public void clearFiles(){
//        attachFiles.clear();
//    }

}
