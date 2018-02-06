import React, {Component} from "react";
import {Button, FormGroup, FormControl, ControlLabel} from "react-bootstrap";
import "./SignUp.css";

export default class SignUp extends Component {
    constructor(props) {
        super(props);

        this.state = {
            user: {
                firstName: "",
                lastName: "",
                emailId: "",
                phoneNumber: "",
                regNumber: ""
            }
        };
    }

    validateForm() {
        return this.state.user.firstName.length > 0 && this.state.user.emailId.length > 0 && /^\d+$/.test(this.state.user.phoneNumber) && this.state.user.phoneNumber.length === 10;
    }

    handleChange = event => {

        var usr = this.state.user;
        usr[event.target.id] = event.target.value;
        this.setState({user: usr})
    }
    handleSubmit = async event => {
        event.preventDefault();
        try {
            await this.register();
            if (this.state.user.regNumber) {
                alert(this.state.user.user.firstName + " have Successfully registerd");
            } else {
                alert("User Already Exist");
            }
        } catch (e) {
            alert(e);
        }
    }

    render() {
        return (
            <div className="SignUp">

                <form onSubmit={this.handleSubmit}>
                    <h3>Registration</h3>
                    <FormGroup controlId="firstName" bsSize="large">
                        <ControlLabel>First Name</ControlLabel>
                        <FormControl
                            autoFocus
                            type="text"
                            value={this.state.user.firstName}
                            onChange={this.handleChange}
                        />
                    </FormGroup>
                    <FormGroup controlId="lastName" bsSize="large">
                        <ControlLabel>Last Name</ControlLabel>
                        <FormControl

                            type="text"
                            value={this.state.user.lastName}
                            onChange={this.handleChange}
                        />
                    </FormGroup>
                    <FormGroup controlId="phoneNumber" bsSize="large">
                        <ControlLabel>Phone</ControlLabel>
                        <FormControl

                            type="tel"
                            value={this.state.user.phoneNumber}
                            onChange={this.handleChange}
                        />
                    </FormGroup>
                    <FormGroup controlId="emailId" bsSize="large">
                        <ControlLabel>Email</ControlLabel>
                        <FormControl
                            value={this.state.user.emailId}
                            onChange={this.handleChange}
                            type="email"
                        />
                    </FormGroup>
                    <Button
                        block
                        bsSize="large"
                        disabled={!this.validateForm()}
                        type="submit"
                    >
                        Register
                    </Button>
                </form>
            </div>
        );
    }

    register() {
        const _t = this;
        fetch('/users', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(this.state.user)
        }).then(result => {
            if (result.status === 200) {
                result.json().then((usr) => {
                    _t.props.hasAuthenticated(true,usr);
                    _t.props.history.push("/");
                });

            }
        });

    }
}