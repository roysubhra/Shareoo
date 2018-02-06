import React, {Component} from 'react';
import logo from './logo.png';
import {Nav, Navbar, NavItem, NavbarBrand, Collapse, NavbarToggler, NavLink} from "reactstrap";
import Routes from "./Routes";
import { withRouter } from "react-router-dom";
import RouteNavItem from "./components/RouteNavItem";
import './App.css';

class App extends Component {
    constructor(props) {
        super(props);

        this.toggleNavbar = this.toggleNavbar.bind(this);

        this.state = {
            collapsed: true,
            isAuthenticated: false,
            usr: {}
        };
    }

    toggleNavbar() {
        this.setState({
            collapsed: !this.state.collapsed
        });
    }

    hasAuthenticated = (authenticated, usr) => {
        this.setState({
            isAuthenticated: authenticated,
            usr: usr
        });
    }

    handleLogout = event => {
        this.hasAuthenticated(false,{});
        this.props.history.push("/login");
    }

    render() {
        const childProps = {
            isAuthenticated: this.state.isAuthenticated,
            hasAuthenticated: this.hasAuthenticated,
            usr: this.state.usr
        };
        return (
            <div className="App container">
                <Navbar color="faded" light expand="md">
                    <img src={logo} className="App-logo" alt="logo"/><NavbarBrand href="/">Shareoo</NavbarBrand>
                    <NavbarToggler onClick={this.toggleNavbar} className="mr-2"/>
                    <Collapse isOpen={!this.state.collapsed} navbar>
                        <Nav className="ml-auto" navbar>
                            {this.state.isAuthenticated
                                ? [ <NavItem>
                                    <NavLink> Welcome <em> {this.state.usr.firstName} {this.state.usr.lastName} </em> </NavLink>
                                    </NavItem>,
                                    <NavItem>
                                    <NavLink href="#" onClick={this.handleLogout}>Logout</NavLink>
                                </NavItem>]
                                :
                                [
                                    <RouteNavItem key={1}>
                                        <NavLink href="/signup">Signup</NavLink>
                                    </RouteNavItem>,
                                    <RouteNavItem key={2}>
                                        <NavLink href="/login">Login</NavLink>
                                    </RouteNavItem>
                                ]}
                        </Nav>
                    </Collapse>
                </Navbar>

                <Routes childProps={childProps}/>
            </div>
        );
    }
}

export default withRouter(App);
