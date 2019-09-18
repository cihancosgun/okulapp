import React, { Component } from 'react';
import {
  StyleSheet,
  TouchableHighlight,
  Alert, KeyboardAvoidingView, Dimensions, AsyncStorage, FlatList, ActivityIndicator, View, ScrollView
} from 'react-native';
import { Container, Header, Text, Content, Item, Input, Icon, List, ListItem, Button, Left, Thumbnail, Image, Body, Right, Title } from 'native-base';
import Moment from 'moment';
import { OkulApi } from '../services/OkulApiService';


export class ChatSubScreen extends React.Component {

  constructor(props) {
    super(props);
    state = {}
  }

  async componentDidMount() { 
    Moment.locale('tr');    
  }

  static navigationOptions = {
    title: 'Mesajlaşma',
  };

  back(){
    this.props.navigation.navigate('Chat');
  }

  renderMessages(){   
    //let thumbUrl = OkulApi.currentChat.convReceiverImage != null ? {uri :  OkulApi.apiURL+'getImage?fileId='+ OkulApi.currentChat.convReceiverImage.$oid } : require('../assets/images/user-profile.png'); 
    const imageComponents = OkulApi.currentChat.messages.map((message, idx)=> 
      <View  key={Math.random()} style={message.senderEmail==OkulApi.userName ? styles.messageSend : styles.messageReceive}>
        <Button key={Math.random()} rounded success={message.senderEmail==OkulApi.userName} info={message.senderEmail!=OkulApi.userName}>
          <Text>{message.message}</Text>
        </Button>
        <Text note>{Moment(new Date(message.sendingTime.$date)).format('DD.MM.YYYY HH:mm:ss')}</Text>
      </View>);
    return (imageComponents);
  }

  render() {
    return (      
        <Container>
            <Header>
              <Left>
                <Button transparent onPress={()=>this.back()}>
                  <Icon name='arrow-round-back' />
                </Button>
              </Left>
              <Body>
                <Title>Mesajlaşma</Title>
              </Body>
              <Right />
            </Header>
            <KeyboardAvoidingView style={{flex:1}} behavior="padding" enabled>
          <Content>                
              <ScrollView style={styles.scrollView}>
                {this.renderMessages()}                
              </ScrollView>           
              <Item style={styles.message}>
                  <Input placeholder='Mesaj..'/>
                  <Icon active name='send'/>
              </Item>                 
          </Content>  
          </KeyboardAvoidingView>          
        </Container>      
    );
  }

  _signOutAsync = async () => {
    await AsyncStorage.clear();
    this.props.navigation.navigate('Auth');
  };
}


const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  scrollView:{
    flex:1,
    height: Dimensions.get('window').height-110,
    backgroundColor:'lightblue',
  },
  messageReceive:{
    alignSelf:'flex-start',
    marginTop:10
  },
  messageSend:{
    alignSelf:'flex-end',
    marginTop:10
  },
  message:{    
  }
});
