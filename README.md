# Live Fishing

This mod adds an extra level of engagement with fishing. When a fish is caught, the actual fish is reeled in instead of the dead item.
FAQ
- How can I add support for my mod?
  Adding your custom fishing rod

In your mod, simply overwrite the item tag data/c/tags/items/fishing_rods.json with your fishing rods
Adding your custom fish

[UNTESTED] Add this mod as an optional dependency through Jitpack or other means and mixin into io.github.simplycmd.fishing.mixin.LiveFishingMixin and inject into the method matchFishEntity
- Will you port this to forge?

![I don't personally know a blacksmith](https://i.imgur.com/tf5W69k.png)

*Though it would be really easy so if you want to do it yourself, feel free to.