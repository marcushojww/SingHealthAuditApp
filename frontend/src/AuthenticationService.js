import axios from "axios";

const API_URL = "http://localhost:8080";

export const USER_NAME_SESSION_ATTRIBUTE_NAME = "authenticatedUser";
export const SESSION_TOKEN = "SESSION_TOKEN";
class AuthenticationService {
  executeJwtAuthenticationService(username, password) {
    return axios.post(`${API_URL}/authenticateP`, {
      username,
      password,
    });
  }

  // registerSuccessfulLogin(username, password) {
  //     //console.log('registerSuccessfulLogin')
  //     sessionStorage.setItem(USER_NAME_SESSION_ATTRIBUTE_NAME, username)
  //     this.setupAxiosInterceptors(this.createBasicAuthToken(username, password))
  // }

  registerSuccessfulLoginForJwt(username, token) {
    sessionStorage.setItem(USER_NAME_SESSION_ATTRIBUTE_NAME, username);
    sessionStorage.setItem(SESSION_TOKEN, this.createJWTToken(token));
    this.setupAxiosInterceptors(this.createJWTToken(token));
  }

  createJWTToken(token) {
    return "Bearer " + token;
  }

  logout() {
    sessionStorage.removeItem(SESSION_TOKEN);
    sessionStorage.removeItem(USER_NAME_SESSION_ATTRIBUTE_NAME);
    this.setupAxiosInterceptors("hello");
    window.location.reload();
  }

  isUserLoggedIn() {
    let user = sessionStorage.getItem(USER_NAME_SESSION_ATTRIBUTE_NAME);

    if (user === null) return false;

    return true;
  }

  getLoggedInUserName() {
    let user = sessionStorage.getItem(USER_NAME_SESSION_ATTRIBUTE_NAME);
    if (user === null) return "";
    return user;
  }

  setupAxiosInterceptors(token) {
    axios.interceptors.request.use((config) => {
      if (this.isUserLoggedIn()) {
        config.headers.authorization = token;
        // console.log("token")
        // console.log(token)
      }
      return config;
    });
  }

  getStoredAxiosInterceptor() {
    axios.interceptors.request.use((config) => {
      if (this.isUserLoggedIn()) {
        config.headers.authorization = sessionStorage.getItem(SESSION_TOKEN);
      }
      return config;
    });
  }
}

export default new AuthenticationService();
