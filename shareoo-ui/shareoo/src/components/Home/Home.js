import React, {Component} from "react";
import "./Home.css";
import { PageHeader, ListGroup, ListGroupItem } from "react-bootstrap";

export default class Home extends Component {
    constructor(props) {
        super(props);

        this.state = {
            isLoading: true,
            shareGroups: []
        };
    }

    async componentDidMount() {
        if (!this.props.isAuthenticated) {
            return;
        }

        try {
           await this.groups();

        } catch (e) {
            alert(e);
        }

        this.setState({ isLoading: false });
    }
    async groups (){
        try {
            if (!this.props.isAuthenticated) {
                return;
            }
            const _t = this;
            console.log(_t.props.usr);
            fetch(`users/` + _t.props.usr.regNumber+"/sharegroups",{ 'method':'GET', headers:{ 'Accept': 'application/json',
                    'Content-Type': 'application/json'}})
                .then(result => {
                    if (result.status === 200) {
                        result.json().then((grps)=>{
                            _t.setState({
                                shareGroups : grps
                            })
                        });

                    }
                    this.setState({ isLoading: false });
                });
        } catch (e) {
            alert(e);
        }

    }
    renderLander() {
        return (
            <div className="Home">
                <div className="lander">
                    <h1>Shareoo</h1>
                    <p>The Cost Sharing Tiger</p>
                </div>
            </div>
        );

    }
    handleCreateNewGroup = () =>{

    }

    handleViewGroup = (groupId) =>{

    }
    renderGroupsList(groups){
        const _t = this;

        return [{}].concat(groups).map(
            (group,i) => {
                console.log(group);
                  return(
                    i !== 0 ?
                      <ListGroupItem
                        key={group.shareGroupId}
                        onClick={this.handleViewGroup}
                        href={`/users/${_t.props.usr.regNumber}/sharegroups/${group.shareGroupId}`}
                        header={group.name}
                    >
                          Members :<span class="members">{group.members.length}</span>  Expenses : <span class="expenses">{group.expenses.length > 0 ? group.expenses.reduce((a,b)=>a.amount+b.amount):0.00}</span>
                    </ListGroupItem>
                        :
                        <ListGroupItem
                            onClick={this.handleCreateNewGroup}
                            key="new"
                            href ="/newgroup"
                        >
                            <h4>
                                <b>{"\uFF0B"}</b> Create a new group
                            </h4>
                        </ListGroupItem>
                  );
            }
        )
    }
    renderGroups() {
        return (
            <div className="notes">
                <PageHeader>Your Groups</PageHeader>
                <ListGroup>
                    {!this.state.isLoading && this.renderGroupsList(this.state.shareGroups)}
                </ListGroup>
            </div>
        );
    }

    render() {
        return (
            this.props.isAuthenticated ? this.renderGroups() : this.renderLander()
        );
    }
}