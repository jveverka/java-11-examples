# Enigma machine
Software implementation of mechanical [Enigma cypher machine](https://en.wikipedia.org/wiki/Enigma_machine).

```
Enigma enigmaForEncryption = Enigma.builder()
        .createEnigmaAlphabetBase64Rotors3(18, 12, 21)
        .build();
Enigma enigmaForDecryption = Enigma.builder()
        .createEnigmaAlphabetBase64Rotors3(18, 12, 21)
        .build();
        
String originalMessage = "Hello World !";       

String encryptedMessage = enigmaForEncryption.encryptGenericString(originalMessage);
String decryptedMessage = enigmaForDecryption.decryptGenericString(encryptedMessage);

assertEquals(originalMessage, decryptedMessage);
assertNotEquals(originalMessage, encryptedMessage);
assertNotEquals(decryptedMessage, encryptedMessage);
```
