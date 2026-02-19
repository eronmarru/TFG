
import re

with open("app/src/main/java/com/example/underworldstrack/DatabaseHelper.java", "r", encoding="utf-8") as f:
    content = f.read()

matches = re.findall(r'insertBandInternal\(\s*db,\s*"([^"]+)",\s*(?:"[^"]*"|\s*\+\s*"[^"]*")*,\s*"([^"]+)",\s*"([^"]+)"\)', content, re.DOTALL)

# The regex above is tricky due to multiline descriptions.
# Let's try a simpler approach by finding the call and then parsing the arguments.

pattern = re.compile(r'insertBandInternal\s*\((.*?)\);', re.DOTALL)
calls = pattern.findall(content)

for call in calls:
    # Split by comma but respect quotes
    # This is a naive split, might fail if commas are inside quotes
    # But descriptions are long strings, likely with commas.
    
    # Better approach: split by `",` which separates arguments
    parts = call.split('",')
    if len(parts) >= 4:
        # Last part is usually image resource: "image")
        # Second to last is faction: "Faction"
        faction_part = parts[-2].strip()
        # Clean up leading quotes/newlines
        faction = faction_part.split('"')[-1]
        
        # Name is the first string literal
        name_match = re.search(r'"([^"]+)"', parts[0])
        name = name_match.group(1) if name_match else "Unknown"
        
        print(f"Band: {name}, Faction: {faction}")

