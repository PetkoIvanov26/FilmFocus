package filmfocus.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import filmfocus.services.UserService;
import filmfocus.testUtils.constants.RoleConstants;
import filmfocus.testUtils.constants.UserConstants;
import filmfocus.testUtils.factories.HttpCookieFactory;
import filmfocus.testUtils.factories.UserFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static filmfocus.testUtils.constants.HttpCookieConstants.COOKIE_NAME;
import static filmfocus.testUtils.constants.HttpCookieConstants.COOKIE_VALUE;
import static filmfocus.testUtils.constants.RoleConstants.NAME;
import static filmfocus.testUtils.constants.UserConstants.DAY;
import static filmfocus.testUtils.constants.UserConstants.EMAIL;
import static filmfocus.testUtils.constants.UserConstants.FIRST_NAME;
import static filmfocus.testUtils.constants.UserConstants.ID;
import static filmfocus.testUtils.constants.UserConstants.JOIN_DATE;
import static filmfocus.testUtils.constants.UserConstants.LAST_NAME;
import static filmfocus.testUtils.constants.UserConstants.MONTH;
import static filmfocus.testUtils.constants.UserConstants.USERNAME;
import static filmfocus.testUtils.constants.UserConstants.YEAR;
import static filmfocus.utils.constants.URIConstants.ADMINS_ID_PATH;
import static filmfocus.utils.constants.URIConstants.ADMINS_PATH;
import static filmfocus.utils.constants.URIConstants.LOGIN_PATH;
import static filmfocus.utils.constants.URIConstants.REGISTRATION_PATH;
import static filmfocus.utils.constants.URIConstants.USERS_ID_PATH;
import static filmfocus.utils.constants.URIConstants.USERS_PATH;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

  public static final String COOKIE = COOKIE_NAME + "=" + COOKIE_VALUE;
  public static final String RETURN_OLD = "returnOld";
  private static final String SET_COOKIE = "Set-Cookie";
  private static final String JOIN_DATE_STRING = "joinDate";
  private static final String IS_BEFORE = "isBefore";
  private static final String RECOVER_PASSWORD_PATH = "/password-recovery";

  private final static ObjectMapper objectMapper = new ObjectMapper();

  private MockMvc mockMvc;

  @Mock
  private UserService userService;

  @InjectMocks
  private UserController userController;

  @Before
  public void setup() {
    mockMvc = MockMvcBuilders
      .standaloneSetup(userController)
      .build();
  }

  @Test
  public void testLogin_cookieObtained_success() throws Exception {
    String json = objectMapper.writeValueAsString(UserFactory.getDefaultUserRequest());
    when(userService.login(any())).thenReturn(HttpCookieFactory.getDefaultHttpCookie());

    mockMvc.perform(post(LOGIN_PATH)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(json))
           .andExpect(status().isOk())
           .andExpect(header().string(SET_COOKIE, COOKIE));
  }

  @Test
  public void testRegisterUser_cookieObtained_success() throws Exception {
    String json = objectMapper.writeValueAsString(UserFactory.getDefaultUserRequest());
    when(userService.registerUser(any())).thenReturn(HttpCookieFactory.getDefaultHttpCookie());

    mockMvc.perform(post(REGISTRATION_PATH)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(json))
           .andExpect(status().isCreated())
           .andExpect(header().string(SET_COOKIE, COOKIE));
  }

  @Test
  public void testRegisterUserByAdmin_cookieObtained_success() throws Exception {
    String json = objectMapper.writeValueAsString(UserFactory.getDefaultAdminRequest());
    when(userService.registerUserByAdmin(any())).thenReturn(HttpCookieFactory.getDefaultHttpCookie());

    mockMvc.perform(post(ADMINS_PATH)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(json))
           .andExpect(status().isCreated())
           .andExpect(header().string(SET_COOKIE, COOKIE));
  }

  @Test
  public void testGetUserByUsername_userFound_success() throws Exception {
    when(userService.getUserDtoByUsername(anyString())).thenReturn(UserFactory.getDefaultUserDto());

    mockMvc.perform(get(USERS_PATH)
                      .queryParam("username", USERNAME))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(ID))
           .andExpect(jsonPath("$.username").value(USERNAME))
           .andExpect(jsonPath("$.email").value(EMAIL))
           .andExpect(jsonPath("$.firstName").value(FIRST_NAME))
           .andExpect(jsonPath("$.lastName").value(LAST_NAME))
           .andExpect(jsonPath("$.joinDate[0]").value(YEAR))
           .andExpect(jsonPath("$.joinDate[1]").value(MONTH))
           .andExpect(jsonPath("$.joinDate[2]").value(DAY))
           .andExpect(jsonPath("$.roles[0].id").value(RoleConstants.ID))
           .andExpect(jsonPath("$.roles[0].name").value(NAME));
  }

  @Test
  public void testGetUserByEmail_userFound_success() throws Exception {
    when(userService.getUserDtoByEmail(anyString())).thenReturn(UserFactory.getDefaultUserDto());

    mockMvc.perform(get(USERS_PATH)
                      .queryParam("email", EMAIL))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(UserConstants.ID))
           .andExpect(jsonPath("$.username").value(USERNAME))
           .andExpect(jsonPath("$.email").value(EMAIL))
           .andExpect(jsonPath("$.firstName").value(FIRST_NAME))
           .andExpect(jsonPath("$.lastName").value(LAST_NAME))
           .andExpect(jsonPath("$.joinDate[0]").value(YEAR))
           .andExpect(jsonPath("$.joinDate[1]").value(MONTH))
           .andExpect(jsonPath("$.joinDate[2]").value(DAY))
           .andExpect(jsonPath("$.roles[0].id").value(RoleConstants.ID))
           .andExpect(jsonPath("$.roles[0].name").value(NAME));
  }

  @Test
  public void testGetUserByRoleName_userFound_success() throws Exception {
    when(userService.getUsersDtoByRoleName(anyString())).thenReturn(UserFactory.getDefaultUserDtoList());

    mockMvc.perform(get(USERS_PATH)
                      .queryParam("roleName", NAME))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].id").value(ID))
           .andExpect(jsonPath("$[0].username").value(USERNAME))
           .andExpect(jsonPath("$[0].email").value(EMAIL))
           .andExpect(jsonPath("$[0].firstName").value(FIRST_NAME))
           .andExpect(jsonPath("$[0].lastName").value(LAST_NAME))
           .andExpect(jsonPath("$[0].joinDate[0]").value(YEAR))
           .andExpect(jsonPath("$[0].joinDate[1]").value(MONTH))
           .andExpect(jsonPath("$[0].joinDate[2]").value(DAY))
           .andExpect(jsonPath("$[0].roles[0].id").value(RoleConstants.ID))
           .andExpect(jsonPath("$[0].roles[0].name").value(NAME));
  }

  @Test
  public void testGetUsersByJoinDate_returnsUsersList_success() throws Exception {
    when(userService.getUsersDtosByJoinDate(any(), anyBoolean())).thenReturn(UserFactory.getDefaultUserDtoList());

    mockMvc.perform(get(USERS_PATH)
                      .queryParam(JOIN_DATE_STRING, String.valueOf(JOIN_DATE))
                      .queryParam(IS_BEFORE, String.valueOf(true)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].id").value(ID))
           .andExpect(jsonPath("$[0].username").value(USERNAME))
           .andExpect(jsonPath("$[0].email").value(EMAIL))
           .andExpect(jsonPath("$[0].firstName").value(FIRST_NAME))
           .andExpect(jsonPath("$[0].lastName").value(LAST_NAME))
           .andExpect(jsonPath("$[0].joinDate[0]").value(YEAR))
           .andExpect(jsonPath("$[0].joinDate[1]").value(MONTH))
           .andExpect(jsonPath("$[0].joinDate[2]").value(DAY))
           .andExpect(jsonPath("$[0].roles[0].id").value(RoleConstants.ID))
           .andExpect(jsonPath("$[0].roles[0].name").value(NAME));
  }

  @Test
  public void testUpdateUser_returnOldTrue_success() throws Exception {
    when(userService.updateUser(any(), anyInt())).thenReturn(UserFactory.getDefaultUserDto());
    String json = objectMapper.writeValueAsString(UserFactory.getDefaultUserRequest());

    mockMvc.perform(put(USERS_ID_PATH, ID)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(json)
                      .queryParam(RETURN_OLD, String.valueOf(true)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(ID))
           .andExpect(jsonPath("$.username").value(USERNAME))
           .andExpect(jsonPath("$.email").value(EMAIL))
           .andExpect(jsonPath("$.firstName").value(FIRST_NAME))
           .andExpect(jsonPath("$.lastName").value(LAST_NAME))
           .andExpect(jsonPath("$.joinDate[0]").value(YEAR))
           .andExpect(jsonPath("$.joinDate[1]").value(MONTH))
           .andExpect(jsonPath("$.joinDate[2]").value(DAY))
           .andExpect(jsonPath("$.roles[0].id").value(RoleConstants.ID))
           .andExpect(jsonPath("$.roles[0].name").value(NAME));
  }

  @Test
  public void testUpdateUser_returnOldFalse_success() throws Exception {
    when(userService.updateUser(any(), anyInt())).thenReturn(UserFactory.getDefaultUserDto());
    String json = objectMapper.writeValueAsString(UserFactory.getDefaultUserRequest());

    mockMvc.perform(put(USERS_ID_PATH, ID)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(json))
           .andExpect(status().isNoContent());
  }

  @Test
  public void testUpdateUserByAdmin_returnOldTrue_success() throws Exception {
    when(userService.updateUserByAdmin(any(), anyInt())).thenReturn(UserFactory.getDefaultUserDto());
    String json = objectMapper.writeValueAsString(UserFactory.getDefaultAdminRequest());

    mockMvc.perform(put(ADMINS_ID_PATH, ID)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(json)
                      .queryParam(RETURN_OLD, String.valueOf(true)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(ID))
           .andExpect(jsonPath("$.username").value(USERNAME))
           .andExpect(jsonPath("$.email").value(EMAIL))
           .andExpect(jsonPath("$.firstName").value(FIRST_NAME))
           .andExpect(jsonPath("$.lastName").value(LAST_NAME))
           .andExpect(jsonPath("$.joinDate[0]").value(YEAR))
           .andExpect(jsonPath("$.joinDate[1]").value(MONTH))
           .andExpect(jsonPath("$.joinDate[2]").value(DAY))
           .andExpect(jsonPath("$.roles[0].id").value(RoleConstants.ID))
           .andExpect(jsonPath("$.roles[0].name").value(NAME));
  }

  @Test
  public void testUpdateUserByAdmin_returnOldFalse_success() throws Exception {
    when(userService.updateUserByAdmin(any(), anyInt())).thenReturn(UserFactory.getDefaultUserDto());
    String json = objectMapper.writeValueAsString(UserFactory.getDefaultAdminRequest());

    mockMvc.perform(put(ADMINS_ID_PATH, ID)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(json))
           .andExpect(status().isNoContent());
  }

  @Test
  public void testDeleteUser_returnOldTrue_success() throws Exception {
    when(userService.deleteUser(anyInt())).thenReturn(UserFactory.getDefaultUserDto());

    mockMvc.perform(delete(USERS_ID_PATH, ID)
                      .queryParam(RETURN_OLD, String.valueOf(true)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(ID))
           .andExpect(jsonPath("$.username").value(USERNAME))
           .andExpect(jsonPath("$.email").value(EMAIL))
           .andExpect(jsonPath("$.firstName").value(FIRST_NAME))
           .andExpect(jsonPath("$.lastName").value(LAST_NAME))
           .andExpect(jsonPath("$.joinDate[0]").value(YEAR))
           .andExpect(jsonPath("$.joinDate[1]").value(MONTH))
           .andExpect(jsonPath("$.joinDate[2]").value(DAY))
           .andExpect(jsonPath("$.roles[0].id").value(RoleConstants.ID))
           .andExpect(jsonPath("$.roles[0].name").value(NAME));
  }

  @Test
  public void testDeleteUser_returnOldFalse_success() throws Exception {
    when(userService.deleteUser(anyInt())).thenReturn(UserFactory.getDefaultUserDto());

    mockMvc.perform(delete(USERS_ID_PATH, ID))
           .andExpect(status().isNoContent());
  }

  @Test
  public void testRecoverPassword_returnsOk_success() throws Exception {
    when(userService.recoverPassword(anyString())).thenReturn(UserFactory.getDefaultUser());

    mockMvc.perform(patch(RECOVER_PASSWORD_PATH)
                      .queryParam("username", USERNAME))
           .andExpect(status().isOk());
  }
}