# CombatLogX
CombatLogX is a simple combat tagging plugin  
You can also abbreviate it as CLX  
For planned features, you can check the 'To Do.list' file

## Version Information
The format is as follows:
*major*.*minor*.*patch*.*expansion*  
*expansion* will change when an expansion is changed or updated  
*patch* will change when I make small changes that don't affect anything major  
*minor* will change when I add a new expansion or create more effective changes  
*major* will change when I modify the API or make a lot of changes that affect the plugin  

## Expansion Information
Expansions are similar to plugins, but instead of going in */plugins/* they go in */plugins/CombatLogX/expansions/*  
They can be used to add new features to CombatLogX or add Compatibility with other plugins  
  
More information about expansions can be found here: [CombatLogX Wiki](https://www.spigotmc.org/wiki/combatlogx/)

## CombatLogX API
The API for this version is not on the global repository. You have to manually install it.  
You can run the following commands as long as you have `Git` and `Maven` installed correctly.  
```cmd
git clone "https://github.com/SirBlobman/SirBlobmanAPI.git" "./SirBlobmanAPI"
git clone "https://github.com/SirBlobman/CombatLogX.git" "./CombatLogX-9" --branch "old/9.0.0.0"
cd "SirBlobmanAPI"
mvn clean install
cd "../CombatLogX-9"
mvn clean install -N
cd "API"
mvn clean install
```  
After you do that, you will be able to use the project in your maven pom.xml

```pom.xml
<dependencies>
    <groupId>com.SirBlobman.combatlogx</groupId>
    <artifactId>CombatLogX-API</artifactId>
    <version>9.0.0.0</version>
    <scope>provided</scope>
</dependencies>
```

---
If you have any suggestions you can contact SirBlobman here:  
**Discord:** https://discord.gg/ZtbqXUK  
**SpigotMC:** [SirBlobman's Profile](https://www.spigotmc.org/members/73161/)
