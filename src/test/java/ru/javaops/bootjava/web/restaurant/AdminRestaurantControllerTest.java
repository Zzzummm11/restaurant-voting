package ru.javaops.bootjava.web.restaurant;

import config.TestConfig;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.bootjava.model.Restaurant;
import ru.javaops.bootjava.repository.RestaurantRepository;
import ru.javaops.bootjava.util.JsonUtil;
import ru.javaops.bootjava.web.AbstractControllerTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.bootjava.web.restaurant.RestaurantTestData.*;
import static ru.javaops.bootjava.web.user.UserTestData.ADMIN_MAIL;
import static ru.javaops.bootjava.web.user.UserTestData.USER_MAIL;

@Import(TestConfig.class)
public class AdminRestaurantControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = AdminRestaurantController.REST_URL + '/';

    @Autowired
    private RestaurantRepository repository;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + RESTAURANT1_ID))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + RESTAURANT1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurant1));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(AdminRestaurantController.REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurants));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + RESTAURANT1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(repository.findById(RESTAURANT1_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void create() throws Exception {
        Restaurant newRestaurant = RestaurantTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(AdminRestaurantController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newRestaurant)))
                .andDo(print())
                .andExpect(status().isCreated());

        Restaurant created = RESTAURANT_MATCHER.readFromJson(action);
        int newId = created.id();
        newRestaurant.setId(newId);
        RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
        RESTAURANT_MATCHER.assertMatch(repository.getExisted(newId), newRestaurant);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createInvalid() throws Exception {
        Restaurant invalid = new Restaurant(null, null);
        perform(MockMvcRequestBuilders.post(AdminRestaurantController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createDuplicate() throws Exception {
        Restaurant expected = new Restaurant(null, RESTAURANT2_NAME);
        perform(MockMvcRequestBuilders.post(AdminRestaurantController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(expected)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        Restaurant updated = RestaurantTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());
        Restaurant actual = repository.getExisted(RESTAURANT1_ID);
        RESTAURANT_MATCHER.assertMatch((Restaurant) Hibernate.unproxy(actual), updated);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateInvalid() throws Exception {
        Restaurant invalid = new Restaurant(restaurant1);
        invalid.setName("");
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = ADMIN_MAIL)
    void updateDuplicate() throws Exception {
        Restaurant updated = new Restaurant(restaurant1);
        updated.setName(RESTAURANT2_NAME);
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isConflict());
    }
}
