import React, { Component } from "react";
import AuthenticationService from "../../AuthenticationService";
import {Redirect} from 'react-router-dom';

class LoginComponent extends Component {
  constructor(props) {
    super(props);

    this.state = {
      username: "",
      password: "",
      loggedIn: false,
      showSuccessMessage: false,
    };

    this.handleChange = this.handleChange.bind(this);
    this.loginClicked = this.loginClicked.bind(this);
  }

  handleChange(event) {
    this.setState({
      [event.target.name]: event.target.value,
    });
  }

  loginClicked() {
    AuthenticationService.executeJwtAuthenticationService(
      this.state.username,
      this.state.password
    )
      .then((response) => {
        AuthenticationService.registerSuccessfulLoginForJwt(
          this.state.username,
          response.data.token
        );
        console.log(response.data.accountType);
        if (response.data.accountType == "Auditor") {
          //this.props.history.push(`/home`);
          //console.log(this.props);
          //this.props.history.push('/');
          this.setState({loggedIn:true})
        } else if (response.data.accountType == "Tenant") {
          this.props.history.push(`/home/t`);
        } else if (response.data.accountType == "Manager") {
          this.props.history.push(`/home/m`);
        }
      })
      .catch(() => {
        this.setState({ showSuccessMessage: false });
        this.setState({ loggedIn: false });
      });
  }

  render() {
    if(this.state.loggedIn){
      return <Redirect to='/'/>
    }
    return (
      <div>
        <h1>Login</h1>
        <div className="container">
          {/*<ShowInvalidCredentials hasLoginFailed={this.state.hasLoginFailed}/>*/}
          {this.state.hasLoginFailed && (
            <div className="alert alert-warning">Invalid Credentials</div>
          )}
          {this.state.showSuccessMessage && <div>Login Sucessful</div>}
          {/*<ShowLoginSuccessMessage showSuccessMessage={this.state.showSuccessMessage}/>*/}
          User Name:{" "}
          <input
            type="text"
            name="username"
            value={this.state.username}
            onChange={this.handleChange}
          />
          Password:{" "}
          <input
            type="password"
            name="password"
            value={this.state.password}
            onChange={this.handleChange}
          />
          <button className="btn btn-success" onClick={this.loginClicked}>
            Login
          </button>
        </div>
      </div>
    );
  }
}

export default LoginComponent;
