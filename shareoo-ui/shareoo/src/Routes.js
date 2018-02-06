import React from "react";
import { Route, Switch } from "react-router-dom";
import Home from "./components/Home/Home";
import NotFound from "./components/NotFound";
import Login from "./components/Login/Login";
import SignUp from "./components/SignUp/SignUp";
import AppliedRoute from "./components/AppliedRoute";
export default ({ childProps }) =>
    <Switch>
        <AppliedRoute  path="/" exact component={Home} props={childProps}/>
        <AppliedRoute  path="/login" exact component={Login} props={childProps}/>
        <AppliedRoute  path="/signup" exact component={SignUp} props={childProps} />
        <Route component={NotFound} />
    </Switch>;