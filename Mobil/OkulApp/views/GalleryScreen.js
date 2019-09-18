import React, { Component } from 'react';
import {
  StyleSheet,
  Text,
  View,
  TextInput,
  Button,
  TouchableHighlight,
  Image,
  Alert, KeyboardAvoidingView, AsyncStorage
} from 'react-native';
import Gallery from 'react-native-image-gallery';
import { OkulApi } from '../services/OkulApiService';

export class GalleryScreen extends React.Component {
  constructor(props) {
    super(props);
    state = { }
  }

  static navigationOptions = {
    title: 'Gallery',
  };

  async componentDidMount() {
     
  }
   

  render() {
    return (
      <View style={{ flex: 1}}>
         <Gallery key="myGallery"
            style={{ flex: 1, backgroundColor: 'black' }}
              images={OkulApi.imageGallery}
          />

        <Button
          onPress={() => this.props.navigation.navigate('App')}
          title="Geri"
        />
      </View>
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
    alignItems: 'center',
    justifyContent: 'center',
  },
});
