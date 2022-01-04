package org.springframework.samples.petclinic.card;

import static org.mockito.ArgumentMatchers.any;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author Rosa Molina
 */


@ExtendWith(SpringExtension.class)
@WebMvcTest(value = CardController.class,
		excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
		excludeAutoConfiguration= SecurityConfiguration.class)

public class CardControllerTest {
    
    @MockBean
	private CardService cardService;

    @Autowired
	private MockMvc mockMvc;

    @BeforeEach
    public void configureMock(){
        Card card = new Card();
        card.setId(99);
        //Mockito.when(cardService.saveCard(any(Card.class))).thenReturn(null);
        Mockito.when(cardService.findCardById(99)).thenReturn(card);
    }

    @WithMockUser(value = "usuarioparacartas", authorities = {"admin"})
    @Test
    void testControllerCard() throws Exception {
        testCardCreationControllerOK();
        testCardCreationControllerWrongName();
        testCardCreationControllerWrongCost();
    }

    private void testCardCreationControllerWrongCost() {
    }

    private void testCardCreationControllerWrongName() {
    }

    private void testCardCreationControllerOK() {
    }


    /**
     * @WithMockUser(value = "spring", authorities = {"admin"})
    @Test
    void test10()  throws Exception {
        testProductCreationControllerOK();
        testProductCreationControllerWrongPrice();
        testProductCreationControllerWrongProductName();
    }

	void testProductCreationControllerOK() throws Exception {
        mockMvc.perform(post("/product/create")
                            .with(csrf())
                            .param("name", "cookies")
                            .param("productType", "Food")
                            .param("price", "2.5"))
                .andExpect(status().isOk())
				.andExpect(view().name("welcome"));
    }

    
	void testProductCreationControllerWrongPrice() throws Exception {
        mockMvc.perform(post("/product/create")
                            .with(csrf())
                            .param("name", "cookies")
                            .param("productType", "Food")
                            .param("price", "-2.5"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("product"))
				.andExpect(view().name("products/createOrUpdateProductForm"));
    }

    
	void testProductCreationControllerWrongProductName() throws Exception {
        mockMvc.perform(post("/product/create")
                            .with(csrf())
                            .param("name", "")
                            .param("productType", "Food")
                            .param("price", "2.5"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("product"))
				.andExpect(view().name("products/createOrUpdateProductForm"));
    }
     */

}
