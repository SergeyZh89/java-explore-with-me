package ru.practicum.category.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import ru.practicum.category.model.dto.CategoryDto;
import ru.practicum.category.model.dto.NewCategoryDto;
import ru.practicum.category.service.CategoryService;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CategoryControllerAdmin.class)
class CategoryControllerAdminTest {
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private CategoryService categoryService;
    @Autowired
    private MockMvc mockMvc;

    CategoryDto categoryDto = new CategoryDto().toBuilder()
            .id(1L)
            .name("category")
            .build();


    @Test
    void addCategory() throws Exception {
        when(categoryService.addCategory(any(NewCategoryDto.class)))
                .thenReturn(categoryDto);

        NewCategoryDto newCategoryDto = new NewCategoryDto("category");
        mockMvc.perform(
                        post("/admin/categories")
                                .content(mapper.writeValueAsString(newCategoryDto))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.name").value("category"));
        verify(categoryService, times(1)).addCategory(any(NewCategoryDto.class));
    }

    @Test
    void patchCategory() throws Exception {
        when(categoryService.patchCategory(any(CategoryDto.class)))
                .thenReturn(categoryDto);

        mockMvc.perform(patch("/admin/categories")
                        .content(mapper.writeValueAsString(categoryDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.name").value("category"));
        verify(categoryService, times(1)).patchCategory(any(CategoryDto.class));
    }

    @Test
    void deleteCategory() throws Exception {
        mockMvc.perform(delete("/admin/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        verify(categoryService, times(1)).deleteCategory(anyLong());
    }
}