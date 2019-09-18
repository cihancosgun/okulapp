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
                <Button style={{alignSelf:'flex-end'}} rounded success>
                  <Text>Success</Text>
                </Button>     

                 <Button style={{alignSelf:'flex-start'}} rounded success>
                  <Text>Success</Text>
                </Button>  


 <Button style={{alignSelf:'flex-end'}} rounded success>
                  <Text>Success</Text>
                </Button>     

                 <Button style={{alignSelf:'flex-start'}} rounded success>
                  <Text>Success</Text>
                </Button>  


 <Button style={{alignSelf:'flex-end'}} rounded success>
                  <Text>Success</Text>
                </Button>     

                 <Button style={{alignSelf:'flex-start'}} rounded success>
                  <Text>Success</Text>
                </Button>  


 <Button style={{alignSelf:'flex-end'}} rounded success>
                  <Text>Success</Text>
                </Button>     

                 <Button style={{alignSelf:'flex-start'}} rounded success>
                  <Text>Success</Text>
                </Button>  


 <Button style={{alignSelf:'flex-end'}} rounded success>
                  <Text>Success</Text>
                </Button>     

                 <Button style={{alignSelf:'flex-start'}} rounded success>
                  <Text>Success</Text>
                </Button>  


 <Button style={{alignSelf:'flex-end'}} rounded success>
                  <Text>Success</Text>
                </Button>     

                 <Button style={{alignSelf:'flex-start'}} rounded success>
                  <Text>Success</Text>
                </Button>  


 <Button style={{alignSelf:'flex-end'}} rounded success>
                  <Text>Success</Text>
                </Button>     

                 <Button style={{alignSelf:'flex-start'}} rounded success>
                  <Text>Success</Text>
                </Button>  


 <Button style={{alignSelf:'flex-end'}} rounded success>
                  <Text>Success</Text>
                </Button>     

                 <Button style={{alignSelf:'flex-start'}} rounded success>
                  <Text>Success</Text>
                </Button>  

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
  message:{    
  }
});
