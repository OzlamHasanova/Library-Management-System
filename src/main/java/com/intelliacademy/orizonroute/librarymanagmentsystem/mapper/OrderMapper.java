//package com.intelliacademy.orizonroute.librarymanagmentsystem.mapper;
//
//import com.intelliacademy.orizonroute.librarymanagmentsystem.dto.OrderDTO;
//import com.intelliacademy.orizonroute.librarymanagmentsystem.model.Order;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.ReportingPolicy;
//
//@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
//public interface OrderMapper {
//
//    @Mapping(source = "student.sif", target = "studentSif")
//    @Mapping(source = "book.id", target = "bookId")
//    OrderDTO toDTO(Order order);
//
//    @Mapping(source = "studentSif", target = "student.sif")
//    @Mapping(source = "bookId", target = "book.id")
//    Order toEntity(OrderDTO orderDTO);
//}
