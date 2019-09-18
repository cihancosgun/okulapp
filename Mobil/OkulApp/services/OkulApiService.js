import React, {
    Component
} from 'react';
import {
    AsyncStorage
} from 'react-native';


export class OkulApi extends React.Component {

    constructor(props) {
        super(props);
    }

    static apiURL = "http://192.168.1.5:8080/OkulApp-web/webresources/api/";
    //static apiURL = "http://192.168.134.36:8080/OkulApp-web/webresources/api/";

    static userName="";
    static pass="";
    static token="";

    static imageGallery = [];

    static currentChat = {};

    static hello() {
        console.log('hello');
    }

    static getToken(user, pass, successCalback, errroCallBack) {
        this.userName=user;
        this.pass = pass;        
        var userRecord = {
            "login": user,
            "password": pass
        };
        var formBody = [];
        for (var property in userRecord) {
            var encodedKey = encodeURIComponent(property);
            var encodedValue = encodeURIComponent(userRecord[property]);
            formBody.push(encodedKey + "=" + encodedValue);
        }
        formBody = formBody.join("&");
        fetch(this.apiURL + 'login', {
            method: 'POST',
            headers: {
                Accept: 'application/json',
                'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
            },
            body: formBody
        }).then((response) => {
            if (response.ok && response.headers.map.authorization != null && successCalback != null) {
                AsyncStorage.setItem('userToken', response.headers.map.authorization).then(() => {
                    this.token = response.headers.map.authorization;
                    successCalback(response.headers.map.authorization);
                })
            } else {
                if (errroCallBack != null) {
                    errroCallBack();
                }
            }
        });
    }

    static getUserRole(successCalback, errroCallBack) {
        AsyncStorage.getItem('userToken').then((res) => {
            fetch(this.apiURL + 'getUserRole', {
                    method: 'GET',
                    headers: {
                        Accept: 'application/json;charset=UTF-8',
                        'Content-Type': 'application/json;charset=UTF-8',
                        'Authorization': res
                    }
                }).then((response) => response.json())
                .then((response) => {
                    if (response.role && successCalback != null) {
                        successCalback(response.role);
                    } else {
                        if (errroCallBack != null) {
                            errroCallBack();
                        }
                    }
                });
        });
    }

    static getBoardOfUser(successCalback, errroCallBack) {
        AsyncStorage.getItem('userName').then((user) => {
            AsyncStorage.getItem('password').then((password) => {
                this.getToken(user, password, (res) => {
                    fetch(this.apiURL + 'getBoardOfUser', {
                            method: 'GET',
                            headers: {
                                Accept: 'application/json;charset=UTF-8',
                                'Content-Type': 'application/json;charset=UTF-8',
                                'Authorization': res
                            }
                        }).then((response) => response.json())
                        .then((response) => {
                            if (response.list && successCalback != null) {
                                successCalback(response.list);
                            } else {
                                if (errroCallBack != null) {
                                    errroCallBack();
                                }
                            }
                        });
                });
            });
        });
    }

    static getConversations(search, successCalback, errroCallBack) {
        AsyncStorage.getItem('userName').then((user) => {
            AsyncStorage.getItem('password').then((password) => {
                this.getToken(user, password, (res) => {
                    fetch(this.apiURL + 'getConversations?searchText='+search, {
                            method: 'GET',
                            headers: {
                                Accept: 'application/json;charset=UTF-8',
                                'Content-Type': 'application/json;charset=UTF-8',
                                'Authorization': res
                            }
                        }).then((response) => response.json())
                        .then((response) => {
                            if (response.list && successCalback != null) {
                                successCalback(response.list);
                            } else {
                                if (errroCallBack != null) {
                                    errroCallBack();
                                }
                            }
                        });
                });
            });
        });
    }


    static getUnreadedBoard(successCalback, errroCallBack) {
        AsyncStorage.getItem('userName').then((user) => {
            AsyncStorage.getItem('password').then((password) => {
                this.getToken(user, password, (res) => {
                    fetch(this.apiURL + 'getUnreadedBoard', {
                            method: 'GET',
                            headers: {
                                Accept: 'application/json;charset=UTF-8',
                                'Content-Type': 'application/json;charset=UTF-8',
                                'Authorization': res
                            }
                        }).then((response) => response.json())
                        .then((response) => {
                            if (response.list && successCalback != null) {
                                successCalback(response.list);
                            } else {
                                if (errroCallBack != null) {
                                    errroCallBack();
                                }
                            }
                        });
                });
            });
        });
    }

    static async getMessageTypeIcon(messageType) {
        userToken = await AsyncStorage.getItem('userToken');
        result = await fetch(this.apiURL + 'getMessageTypeIcon/?messageType' + messageType, {
            method: 'GET',
            headers: {
                Accept: 'image/png',
                'Content-Type': 'image/png',
                'Authorization': res
            }
        });
        console.log(result);
        return result;
    }
}