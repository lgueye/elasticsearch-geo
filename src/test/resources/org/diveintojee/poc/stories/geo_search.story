Geo Search stories

Meta:
@refs 1
@progress wip

Narrative:
Given I provide a location
When I search for a station around that location
I should find stations ordered by distance

Scenario: search stations by location, ordered by distance
When I search the closest stations to "10 rue La Fayette 75009, Paris"
Then I should get the following stations:
| id   | name                          | type  |
| 1957 | Chaussée d'Antin (La Fayette) | metro |
| 1638 | Trinité-d'Estienne d'Orves    | metro |
| 1771 | Opéra                         | metro |
| 1744 | Quatre Septembre              | metro |
| 1990 | Auber                         | rer   |
| 1665 | Richelieu-Drouot              | metro |
| 1767 | Notre-Dame de Lorette         | metro |
| 1795 | Le Peletier                   | metro |
| 1852 | Havre-Caumartin               | metro |
| 1686 | Saint-Georges                 | metro |

