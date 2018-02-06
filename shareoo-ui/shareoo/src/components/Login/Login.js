import React, {Component} from "react";
import {Button, FormGroup, FormControl, ControlLabel} from "react-bootstrap";
import "./Login.css";

export default class Login extends Component {
    constructor(props) {
        super(props);

        this.state = {
            phone: "",
            password: "",
            loginSuccess: false
        };
    }

    validateForm() {
        return this.state.phone.length === 10 && this.state.password.length > 0 && /^\d+$/.test(this.state.phone);
    }

    handleChange = event => {
        this.setState({
            [event.target.id]: event.target.value
        });
    }
    handleSubmit = async event => {
        event.preventDefault();
        try {
            const _t = this;
            fetch(`users/` + _t.state.phone,{ 'method':'GET', headers:{ 'Accept': 'application/json',
                    'Content-Type': 'application/json'}})
                .then(result => {
                    if (result.status === 200) {
                        result.json().then((usr)=>{
                            _t.props.hasAuthenticated(true,usr);
                             this.props.history.push("/");
                        });
                    }else {
                        alert("User Does not Exist");
                    }
                });
        } catch (e) {
            alert(e);
        }
    }

    render() {
        return (
            <div className="Login">

                <form onSubmit={this.handleSubmit}>
                    <h3>Login</h3>
                    <FormGroup controlId="phone" bsSize="large">
                        <ControlLabel>Phone</ControlLabel>
                        <FormControl
                            autoFocus
                            type="tel"
                            value={this.state.phone}
                            onChange={this.handleChange}
                        />
                    </FormGroup>
                    <FormGroup controlId="password" bsSize="large">
                        <ControlLabel>Password</ControlLabel>
                        <FormControl
                            value={this.state.password}
                            onChange={this.handleChange}
                            type="password"
                        />
                    </FormGroup>
                    <Button
                        block
                        bsSize="large"
                        disabled={!this.validateForm()}
                        type="submit"
                    >
                        Login
                    </Button>
                </form>
            </div>
        );
    }


}