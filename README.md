# Simple-MIDI-Translator
## Summary
This program enables the remapping of CC/PC instructions coming from one MIDI device and sends them to another. They can be sent to a virtual loop-back MIDI device so it behaves and appears like any other native MIDI device on your PC.

While I have not tested this application on macOS or Linux, because it only uses Java's standard class libraries there is no reason it shouldn't work.

In order to use this application you will need to have a virtual loop-back MIDI device installed. The one I used for my testing was Tobias Erichsen's [loopMIDI](https://www.tobias-erichsen.de/software/loopmidi.html) although any virtual MIDI device should work. Theoretically you could send the remapped instructions to a physical device but since it didn't fit my use case I didn't bother testing it.

## Running the Application
To run the application either compile from source or download the binary from the releases tab. Extract the zip then navigate to the directory the JAR file is located in using your CLI of choice. From there run:

`java -jar SimpleMIDITranslator.jar`

Upon starting the application you will be prompted to select the incoming and outgoing MIDI devices that will be used.

## Configuring the Instruction Mappings
To configure what instructions map to you'll need to edit the **mappings.txt** file which should be located in the same directory as the JAR file.

Each instruction will consist of three components: a status code, a PC or CC instruction, and a velocity value

**Note: velocity is only present for CC instructions! For PC instructions you will only provide a status code and an instruction**

For information on what each value corresponds to [use this](https://www.midi.org/specifications-old/item/table-2-expanded-messages-list-status-bytes).

For our purposes, each part of the MIDI code (status, instruction, and velocity) is separated by a comma. So, using our [table](https://www.midi.org/specifications-old/item/table-2-expanded-messages-list-status-bytes), the instruction CC 22 on channel 1 with velocity 55 (176 22 55) would translate to `176,22,55`. PC instruction 7 on channel 2 (193 7) would translate to `193,7`.

Now to map MIDI codes. The first code you'll list is the original instruction coming from the input MIDI device while the second code will be the one it is "translating" to. To separate the two codes we use the `|` (pipe) character. There are no spaces or tabs anywhere in our config file.

For example, to map the incoming instruction CC 22 on channel 1 with velocity 55 (176 22 55) to the outgoing PC instruction 7 on channel 2 (193 7) we would write the following:

`176,22,55|193,7`

To list other mappings simply hit return after each mapping:

```
176,22,55|193,7
192,1|176,69,0
192,2|176,69,1
192,3|176,69,2
```

Restarting the program after editing the **mapping.txt** file will load your changes.

**Note: any unmapped instructions will be passed along unchanged to the outgoing device.**

## Rant
I'd like to give a special thanks to MeloAudio for designing a quality audio interface with the omission of the ability to set custom velocity values for CC instructions. Of course that wouldn't have been enough had Line6 not doubled down and made Helix Native's MIDI bindings for snapshot and preset switching static. And if that wasn't enough PC instruction support was dropped from VST3. So the **only** way to get preset switching support in Helix Native is to use it on macOS using the AU/AAX version of the plugin. I guess jury-rigging snapshot switching is the next best thing... I wouldn't have spent hours on this without you guys <3
