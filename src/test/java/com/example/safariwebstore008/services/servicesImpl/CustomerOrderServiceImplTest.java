package com.example.safariwebstore008.services.servicesImpl;

import com.example.safariwebstore008.SafariWebstore008Application;
import com.example.safariwebstore008.dto.CheckoutDto;
import com.example.safariwebstore008.enums.*;
import com.example.safariwebstore008.models.*;
import com.example.safariwebstore008.repositories.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SafariWebstore008Application.class)
class CustomerOrderServiceImplTest {
    @Autowired
    private  CustomerOrderRepository repository;
    @Autowired
    UserRepository getUserModelRepository;
    @Autowired
    CartRepository getCartRepository;
    @Autowired
    UserRepository userModelRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    CartIemRepository cartItemRepository;
    @InjectMocks
    DeleteCartSeviceImpl deleteCartSevice;
    @Mock
    private OrderDetailsRepository orderDetailsRepository;
    @Autowired
    OrderDetailsRepository detailsRepository;

@Mock
private CustomerOrderRepository customerOrderRepository;
@Mock
private ShippingRepository shippingRepository;
    @InjectMocks
    private CustomerOrderServicesImpl customerOrderService;
    @Test
    void createACustomerOrder() {
        CheckoutDto checkoutDto= new CheckoutDto();
        String email="richy@gmail.com";
        User user = new User();
        user.setPassword("1234");
        user.setGender(Gender.MALE);
        user.setFirstName("tom");
        user.setLastName("jack");
        user.setEmail(email);
        getUserModelRepository.save(user);
        Cart cart = new Cart();
        cart.setUserModel(user);
        getCartRepository.save(cart);
        CartItem cartItem1= new CartItem();
        cartItem1.setId(1l);
        CartItem cartItem2= new CartItem();
        cartItem2.setId(2l);
        List<CartItem> cartItemList= new ArrayList<>();
        cartItemList.add(cartItem1);
        cartItemList.add(cartItem2);
        cart.setCartItemList(cartItemList);



        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setDeliveryStatus(DeliveryStatus.PENDING);
        customerOrder.setStatus(OrderAssigStatus.UNASSIGNED);
        customerOrder.setDeliveryMethod(DeliveryMethod.DOOR_DELIVERY);
        customerOrder.setUserModel(user);
        OrderDetails orderDetails1= new OrderDetails();
        detailsRepository.save(orderDetails1);



        List<OrderDetails>orderDetailsList= new ArrayList<>();
        orderDetailsList.add(orderDetails1);
        customerOrder.setOrderDetailsList(orderDetailsList);
        CustomerOrder order1=repository.save(customerOrder);
        assertEquals(1,order1.getOrderDetailsList().size());
    }
    @Test
    void customerOrderHistoryTest(){


        String email= "nono@gmail.com";
        User user= new User();
        user.setEmail(email);
        user.setPassword("1234");
        user.setFirstName("ben");
        user.setLastName("tom");
        user.setRoles(Roles.CUSTOMER);
        user.setGender(Gender.MALE);
        userModelRepository.save(user);
        int pageNo=0;
        int pageSize=2;
        String sortBy= "deliveryDate";
        Pageable pageable = PageRequest.of(pageNo,pageSize,Sort.by(sortBy).descending());
        CustomerOrder customerOrder1= new CustomerOrder();
        customerOrder1.setUserModel(user);
        repository.save(customerOrder1);
        CustomerOrder customerOrder2= new CustomerOrder();
        customerOrder2.setUserModel(user);
        repository.save(customerOrder2);
        CustomerOrder customerOrder3= new CustomerOrder();
        customerOrder3.setUserModel(user);
        repository.save(customerOrder3);

        Page<CustomerOrder> customerOrderPage= repository.findAllByUserModel(user,pageable);
        List<CustomerOrder>customerOrderList =customerOrderPage.toList();
        assertEquals(2,customerOrderList.size());

    }

    @Test
    public void findCustomerOrderTest() {

        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setId(1L);
        customerOrder.setDeliveryFee(BigInteger.valueOf(500l));
        when(customerOrderRepository.findById(1L)).thenReturn(java.util.Optional.of(customerOrder));
        assertEquals(customerOrder, customerOrderService.findParticularCustomerOrder(1L));
    }
}