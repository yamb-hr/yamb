import React, { Component } from 'react';
import './label.css';
import { ToastContainer, toast } from 'react-toastify';

class Label extends Component {

    constructor(props) {
        super(props)
        this.state = {}
        this.handleClick = this.handleClick.bind(this);
    }

    handleClick() {
        if (this.props.info)  {
            console.log(this.props.info);
            toast.info(this.props.info, {
                position: "top-center",
                autoClose: 2000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                pauseOnFocusLoss: true,
                draggable: true,
                progress: undefined,
                theme: "dark"
            });
        }
    }

    render() {
        let value = this.props.value;
        let icon = this.props.icon;
        let labelClass = "label " + (this.props.variant ? this.props.variant : "");
        return (
            <div className={labelClass}>
                <button className={labelClass} onClick={this.handleClick}>
                    {icon ? <img src={"./svg/labels/" + icon + ".svg"} alt={value}></img> : <strong>{value}</strong>}

                </button>   
                <ToastContainer limit={10} />
            </div>   
        );
    }    
}

export default Label;