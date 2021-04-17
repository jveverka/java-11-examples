# Enigma machine
Software implementation of mechanical [Enigma cypher machine](https://en.wikipedia.org/wiki/Enigma_machine).

```
Alphabet alphabet = Alphabet.buildAlphabetBase64();
int[] initialRotorPositions = new int[3];
Character[][] plugBoardSetup = Utils.generateRandomPlugBoadrSetup(alphabet);
initialRotorPositions[0] = 18;
initialRotorPositions[1] = 12;
initialRotorPositions[2] = 21;
Enigma enigmaForEncryption = EnigmaFactory.createEnigmaBase64(initialRotorPositions, plugBoardSetup);
Enigma enigmaForDecryption = EnigmaFactory.createEnigmaBase64(initialRotorPositions, plugBoardSetup);

String originalMessage = "Hello World !";       
String encryptedMessage = enigmaForEncryption.encryptGenericString(originalMessage);
String decryptedMessage = enigmaForDecryption.decryptGenericString(encryptedMessage);
```
