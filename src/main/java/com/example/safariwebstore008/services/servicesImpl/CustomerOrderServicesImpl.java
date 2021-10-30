package com.example.safariwebstore008.services.servicesImpl;

import com.example.safariwebstore008.dto.CheckoutDto;
import com.example.safariwebstore008.enums.DeliveryMethod;
import com.example.safariwebstore008.enums.DeliveryStatus;
import com.example.safariwebstore008.enums.OrderAssigStatus;
import com.example.safariwebstore008.exceptions.CustomerOrderNotFoundException;
import com.example.safariwebstore008.models.*;
import com.example.safariwebstore008.repositories.*;
import com.example.safariwebstore008.models.*;
import com.example.safariwebstore008.repositories.*;
import com.example.safariwebstore008.services.CustomerOrderServices;
import com.example.safariwebstore008.services.DeleteCartService;
import com.example.safariwebstore008.services.DeleteCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class CustomerOrderServicesImpl implements CustomerOrderServices {
    @Autowired
    private ShippingRepository shippingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CustomerOrderRepository customerOrderRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;
    @Autowired
    private DeleteCartService deleteCartService;


        public CustomerOrderServicesImpl(CustomerOrderRepository customerOrderRepository) {
            this.customerOrderRepository = customerOrderRepository;
        }

        @Override
        public List<CustomerOrder> getAllDeliveries(int pageNo, int pageSize, String sortBy) {
            Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
            Page<CustomerOrder> pagedResult = customerOrderRepository.findAll(pageable);

            return pagedResult.hasContent() ? pagedResult.getContent() : new ArrayList<>();


        }

        @Override
        public List<CustomerOrder> getAllPendingDeliveries(int pageNo, int pageSize, String sortBy) {
            Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
            Page<CustomerOrder> pagedResult = customerOrderRepository.findByDeliveryStatus(DeliveryStatus.PENDING, pageable);

            return pagedResult.hasContent() ? pagedResult.getContent() : new ArrayList<>();
        }

        @Override
        public List<CustomerOrder> getAllCompletedDeliveries(int pageNo, int pageSize, String sortBy) {
            Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
            Page<CustomerOrder> pagedResult = customerOrderRepository.findByDeliveryStatus(DeliveryStatus.DELIVERED, pageable);

            return pagedResult.hasContent() ? pagedResult.getContent() : new ArrayList<>();
        }

        @Override
        public CustomerOrder createACustomerOrder(CheckoutDto checkoutDto, String email) {
            CustomerOrder customerOrder = new CustomerOrder();
            User user = userRepository.findUserByEmail(email).get();
            Cart cart = cartRepository.findCartByUserModel(user).get();
            List<CartItem> cartItemList = cart.getCartItemList();
            List<OrderDetails> orderDetailsList = new ArrayList<>();
            for (CartItem item : cartItemList) {
                OrderDetails orderDetails = new OrderDetails();
                orderDetails.setPrice(item.getPrice());
                orderDetails.setProduct(item.getProduct());
                orderDetails.setCreateDate(LocalDateTime.now());
                orderDetails.setQuantity(item.getQuantity());
                orderDetailsList.add(orderDetails);
                OrderDetails orderDetails1 = orderDetailsRepository.save(orderDetails);
            }
            deleteCartService.emptyCart(cart);
            customerOrder.setUserModel(user);
            customerOrder.setOrderDetailsList(orderDetailsList);
            customerOrder.setTotalOrderAmount(checkoutDto.getTotalOrderAmount());
            customerOrder.setDeliveryFee(checkoutDto.getDeliveryFee());
            customerOrder.setDeliveryStatus(DeliveryStatus.PENDING);
            customerOrder.setStatus(OrderAssigStatus.UNASSIGNED);
            customerOrder.setDeliveryMethod(DeliveryMethod.DOOR_DELIVERY);

            return customerOrderRepository.save(customerOrder);
        }

        @Override
        public List<CustomerOrder> viewCustomerOrderHistory(String email, int pageNo, int pageSize, String sortBy) {
            User userModel = userRepository.findUserByEmail(email).get();
            Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
            Page<CustomerOrder> customerOrderPage = customerOrderRepository.findAllByUserModel(userModel, pageable);
            return customerOrderPage.toList();
        }

        @Override
        public ResponseEntity<Map<String, Object>> getAllAssignedOrder(int pageNo, int pageSize, String sortBy) {
            Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
            Page<CustomerOrder> orderPage = customerOrderRepository.findByStatus(OrderAssigStatus.ASSIGNED, pageable);
            return getMapResponseEntity(orderPage);
        }

        @Override
        public ResponseEntity<Map<String, Object>> getAllUnassignedOrder(int pageNo, int pageSize, String sortBy) {
            Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
            Page<CustomerOrder> orderPage = customerOrderRepository.findByStatus(OrderAssigStatus.UNASSIGNED, pageable);
            return getMapResponseEntity(orderPage);
        }

        private ResponseEntity<Map<String, Object>> getMapResponseEntity(Page<CustomerOrder> orderPage) {
            if (orderPage.getContent().isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            Map<String, Object> response = new HashMap<>();
            response.put("customerOrder", orderPage.getContent());
            response.put("currentPage", orderPage.getNumber());
            response.put("totalItems", orderPage.getTotalElements());
            response.put("totalPage", orderPage.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

    @Override
    public CustomerOrder findParticularCustomerOrder(Long id) {
        return customerOrderRepository.findById(id).orElseThrow(() -> new CustomerOrderNotFoundException(id));
    }
    }

