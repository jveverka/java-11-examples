# Enigma machine
Software implementation of mechanical [Enigma cypher machine](https://en.wikipedia.org/wiki/Enigma_machine).

```
int[] initialRotorPositions = new int[3];
initialRotorPositions[0] = 18;
initialRotorPositions[1] = 12;
initialRotorPositions[2] = 21;
Enigma enigmaForEncryption = Enigma.builder().createEnigmaBase64(initialRotorPositions).build();
Enigma enigmaForDecryption = Enigma.builder().createEnigmaBase64(initialRotorPositions).build();
        
String originalMessage = "Hello World !";       
String encryptedMessage = enigmaForEncryption.encryptGenericString(originalMessage);
String decryptedMessage = enigmaForDecryption.decryptGenericString(encryptedMessage);
```
